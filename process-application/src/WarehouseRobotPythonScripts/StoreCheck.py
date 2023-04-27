"""
Das Python Skript "StoreCheck.py" überprüft, ob die Einlagerung eines Items am gewünschten Platz möglich ist.
1. Lokales Netzwerkt mit dediziertem MySQL Server, der die Verwaltung der Lagers übernimmt.
2. Verbindung zur Datenbank aufbauen
3. Prozessvariablen aus Camunda übernehmen
4. SELECT Befehl an die Datenbank mit den erhaltenen Prozessvariablen
5. Speichern des Status in einer Variable
6. Status Übergabe zurück an Camunda    
"""

from logging import error
import mysql.connector
from mysql.connector import errorcode
from camunda.external_task.external_task import ExternalTask, TaskResult
from camunda.external_task.external_task_worker import ExternalTaskWorker

default_config = {
    "maxTasks": 1,
    "lockDuration": 10000,
    "asynchResponseTimeout" : 5000,
    "retries": 3,
    "retryTimeout": 5000,
    "sleepSeconds": 30
}

def handle_task(task:ExternalTask) -> TaskResult:
    try:
        piMySqlDB = mysql.connector.connect(
            host = 'localhost',
            user = 'dynamic',
            password='Camunda2021!',
            database='warehouse'
    )

        print(piMySqlDB)

        mycursor = piMySqlDB.cursor()
    
        shelf_id = task.get_variable("shelf_id")
        place_id = task.get_variable("place_id")

        # Status mit für die Einlagerung eines neues Items auf 0 stehen.
        sql_select_check = f"SELECT status FROM place WHERE shelf_id = {shelf_id} AND place_id = {place_id}"

        mycursor.execute(sql_select_check)
        data = mycursor.fetchone()
        status = int(data[0])

        print(shelf_id)
        print(place_id)
        print(status)

    except mysql.connector.Error as err:
        if err.errno == errorcode.ER_ACCESS_DENIED_ERROR:
            print(err)
            return task.failure(error_message="DB ACCESS DENIED", error_details="Cant connect to database")
        elif err.errno == errorcode.ER_BAD_DB_ERROR:
            print(err)
            return task.failure(error_message="DB did not exists", error_details="No Database! BAD ERROR")
        else:
            print(err)
            return task.failure(error_message=err, error_details=err)

    else:
        piMySqlDB.close()

    if status != 0:
        return task.bpmn_error(error_code="PLACE_NOT_EMPTY", error_message="Storage not possible, because place is not empty!!!",variables={"check_success":False})

    return task.complete({"check_success": True})

if __name__ == '__main__':
    ExternalTaskWorker(worker_id="CheckWorker1", config=default_config).subscribe("storecheck", handle_task)

    






    





    