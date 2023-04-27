CREATE DATABASE IF NOT EXISTS rpa_demonstrator;
USE rpa_demonstrator;
CREATE TABLE IF NOT EXISTS Orders (
	ProcessId VARCHAR(36) NOT NULL,
	businessKey VARCHAR(128) NOT NULL,
	OrderId INTEGER NOT NULL,
	Status TEXT NOT NULL,
	OrderDate DATE NOT NULL,
	LatestDeliveryDate DATE NOT NULL,
	ExpectedTruckArrivalDate DATE NULL,
	ExpectedDeliveryDate DATE NULL,
	TruckArrivalDate DATE DEFAULT NULL,
	DeliveryDate DATE DEFAULT NULL,
	UNIQUE INDEX (ProcessId),
	UNIQUE INDEX (OrderId)
);
CREATE TABLE IF NOT EXISTS EngineMessages (
	MessageGuid VARCHAR(36) NOT NULL,
	MessageTime BIGINT NOT NULL,
	Pending BOOLEAN NOT NULL,
	Content TEXT NOT NULL,
	UNIQUE INDEX (MessageGuid)
);