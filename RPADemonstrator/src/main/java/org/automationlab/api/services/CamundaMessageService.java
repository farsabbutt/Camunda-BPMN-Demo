package org.automationlab.api.services;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse.BodyHandlers;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import org.automationlab.api.database.DatabaseContext;
import org.automationlab.api.database.interfaces.IMessageRepository;
import org.automationlab.api.database.models.EngineMessage;
import org.automationlab.api.services.dto.CamundaMessageDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

import com.google.gson.Gson;


public class CamundaMessageService extends TimerTask implements ICamundaMessageService {

    private final Logger _logger = LoggerFactory.getLogger(CamundaMessageService.class);
	
	private final String baseUrl;
	
	private final HttpClient httpClient;
	
	private final Timer timer;
	
	private final Environment env;
	
	public CamundaMessageService(Environment env) {
		this.baseUrl = env.getProperty("camundaEngineApi");
		this.env = env;
		httpClient = HttpClient.newHttpClient();
		timer = new Timer();
		timer.schedule(this, 10000, 10000);
		_logger.info("Message service started");
	}
	
	@Override
	public void run() {	
		IMessageRepository messageRepository = null;
		try {
			messageRepository = new DatabaseContext(env);
			ArrayList<EngineMessage> messages = messageRepository.getPendingMessages();
			if(messages.size() == 0) {
				return;
			}
			int messagesSent = 0;
			int messagesWaiting = 0;
			int messagesError = 0;
			for(EngineMessage message  : messages) {
				try {
					if(System.currentTimeMillis() - message.messageTime > 10000){				
						ServiceResponse<Object> response = sendMessage(message.content);
						if(response.success) {
							message.pending = false;
							messageRepository.updateMessage(message);
							messagesSent++;
						}else{
							messagesError++;
						}
					}else {
						messagesWaiting++;
					}
				}catch(Exception ee){
					_logger.error(ee.getMessage());
				}
			}
			_logger.info("Messages service statistics:");
			_logger.info("	Messages sent: " + messagesSent);
			_logger.info("	Messages waiting: " + messagesWaiting);
			_logger.info("	Messages error: " + messagesError);
			_logger.info("										");
		}catch(Exception e){
			_logger.error(e.getMessage());
		}
		finally {
			try {
				messageRepository.destroy();
			} catch (SQLException e) {
				_logger.error(e.getMessage());
			}
		}
	}
	
	public ServiceResponse<Object> registerMessage(CamundaMessageDto model) {
		ServiceResponse<Object> serviceResponse = new ServiceResponse<Object>();
		IMessageRepository messageRepository = null;
		try {
			messageRepository = new DatabaseContext(env);
			EngineMessage message = new EngineMessage();
			message.messageGuid = UUID.randomUUID().toString();
			message.messageTime = System.currentTimeMillis();
			message.pending = true;
			message.content = new Gson().toJson(model);
			messageRepository.addMessage(message);
		}catch(Exception e){
			serviceResponse.success = false;
			serviceResponse.message = e.getMessage();
			_logger.error(serviceResponse.message);
		}
		finally {
			try {
				messageRepository.destroy();
			} catch (SQLException e) {
				_logger.error(e.getMessage());
			}
		}
		return serviceResponse;
	}
	
	public ServiceResponse<Object> sendMessage(CamundaMessageDto model) {
		return sendMessage(new Gson().toJson(model));
	}
	
	private ServiceResponse<Object> sendMessage(String content) {
		ServiceResponse<Object> serviceResponse = new ServiceResponse<Object>();
		try {			
			Builder requestBuilder = HttpRequest.newBuilder()
					.uri(URI.create(baseUrl + "/message"))
					.header("Content-Type", "application/json")
			        .POST(BodyPublishers.ofString(content));

			HttpResponse<String> httpResponse = httpClient.send(requestBuilder.build(), BodyHandlers.ofString());
											
			if(httpResponse.statusCode() / 100 != 2) {
				serviceResponse.success = false;
				serviceResponse.message = httpResponse.statusCode() + " : " + httpResponse.body();
			}
		}catch(Exception e) {
			serviceResponse.success = false;
			serviceResponse.message = e.getMessage();
			_logger.error(serviceResponse.message);
		}
		return serviceResponse;
	}
}
