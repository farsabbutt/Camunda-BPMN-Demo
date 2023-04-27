#! /usr/bin/env python3
# -*- coding: utf-8 -*-
#intern python for TXT Controller

import sys
import Remote_control_WarehouseRobot
import mysql.connector
from camunda.external_task.external_task import ExternalTask, TaskResult
from camunda.external_task.external_task_worker import ExternalTaskWorker

# Konfiguration ExternalTaskHandler
default_config = {
    "maxTasks": 1,
    "lockDuration": 10000,
    "asyncResponseTimeout": 5000,
    "retries": 3,
    "retryTimeout": 5000,
    "sleepSeconds": 30
}

# Konfiguration MySQL Verbindung Lokales Netzwerk!
warehouse_db = mysql.connector.connect(
        host = 'localhost',
        user = 'ftrobot',
        password='Camunda2021!',
        database='warehouse'
)

print(warehouse_db)

# TODO Fehler bei der Bearbeitung der Aufgabe abfangen und robot_sucess False zurück geben. BPMN Modell muss angepasst werden


def handle_task(task: ExternalTask) -> TaskResult:
    """
    handle_task ist die Hauptfunktion für den TXT Controller
    Die zu bearbeitenden Aufagbe und damit die auszuführende Aktion des Roboters hängt vom jeweiligen Topic und dessen Namen ab.
    Die Aufgabe und die benötigten Variblen der Prozessinstanz wie place_id, item und der Process_ID werden über ExternalTask geholt.
    Nach erfolgreicher Bearbeitung wird ein Update auf der MySQL Datenbank durchgeführt und als Taskresult die 
    Args:
        task (ExternalTask): siehe Modul camunda_external_task_client-python/camunda/external_task.

    Returns:
        TaskResult: siehe Modul camunda_external_task_client-python/camunda/external_task
                    robot_success gibt an ob die Bearbeitung erfolgreich abgeschlossen worden ist.
    """
    task_place_id = task.get_variable("place_id")
    task_item = task.get_variable("item")
    mycursor = warehouse_db.cursor()
    
    if task.get_topic_name() == "storetopic":
        while Remote_control_WarehouseRobot.store_item(task_place_id,task.get_process_instance_id(),task.get_topic_name(),task_item ) == False:
            continue
        else:
            sql_update = f"UPDATE place SET status = 1, item = '{task_item}', last_user = 'Warehouse Robot 1' WHERE place_id = {task_place_id}" 
            print("Einlagern wurde vom Roboter bearbeitet")
        
    
    if task.get_topic_name() == "exstoretopic":
        while Remote_control_WarehouseRobot.exstore_item(task_place_id,task.get_process_instance_id(),task.get_topic_name(),task_item ) == False:
            continue
        else:
            sql_update = f"UPDATE place SET status = 0, item = 'empty', last_user = 'Warehouse Robot 1' WHERE place_id = {task_place_id}" 
            print("Auslagern wurde vom Roboter bearbeitet")

    mycursor.execute(sql_update)
    warehouse_db.commit()

    return task.complete({"robot_success": True})

if __name__ == '__main__':
   ExternalTaskWorker(worker_id="WarehouseRobot1", config=default_config).subscribe({"storetopic","exstoretopic"}, handle_task)
