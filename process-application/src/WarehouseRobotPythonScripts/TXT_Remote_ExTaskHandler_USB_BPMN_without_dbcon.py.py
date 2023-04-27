#! /usr/bin/env python3
# -*- coding: utf-8 -*-
#intern python for TXT Controller


import TXT_Remote_Control_USB_without_dbcon
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
    
    if task.get_topic_name() == "storetopic":
        while TXT_Remote_Control_USB_without_dbcon.store_item(task_place_id,task.get_process_instance_id(),task.get_topic_name(),task_item ) == False:
            continue
        else: 
            print("Einlagern wurde vom Roboter bearbeitet")
        
    
    if task.get_topic_name() == "exstoretopic":
        while TXT_Remote_Control_USB_without_dbcon.exstore_item(task_place_id,task.get_process_instance_id(),task.get_topic_name(),task_item ) == False:
            continue
        else:
            print("Auslagern wurde vom Roboter bearbeitet")

    return task.complete({"robot_success": True})

if __name__ == '__main__':
   ExternalTaskWorker(worker_id="WarehouseRobot1", config=default_config).subscribe({"storetopic","exstoretopic"}, handle_task)
