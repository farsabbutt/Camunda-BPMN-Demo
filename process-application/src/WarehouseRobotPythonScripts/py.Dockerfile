FROM python:3.9

COPY . .

COPY requirements.txt requirements.txt

RUN apt-get update

RUN apt-get install python3-dev default-libmysqlclient-dev gcc  -y

#RUN pip freeze > requirements.txt
RUN pip install camunda-external-task-client-python3

RUN pip install basic-auth

RUN pip install pydantic

RUN python -m pip install --upgrade pip

RUN pip install pandas

#RUN pip install BaseModel

RUN pip install ftrobopy

RUN pip install mysql.connector

RUN pip install mysql-connector-python

RUN pip install -r requirements.txt

RUN chmod a+x pythonScripts.sh

#RUN pip install -r pythonScripts.sh

EXPOSE 5000

CMD [ "./pythonScripts.sh" ]