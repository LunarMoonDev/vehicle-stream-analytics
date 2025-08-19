default: help

help:
	@echo "Makefile for managing Kafka Connect servers"
	@echo "Available commands:"
	@echo "  run_servers     - Start Kafka Connect servers"
	@echo "  show_servers    - List running Kafka Connect servers"
	@echo "  stop_servers    - Stop Kafka Connect servers and remove containers"
	@echo "  show_connect_logs - Show logs of Kafka Connect server"
	@echo "  show_minio_logs - Show logs of MinIO server"
	@echo "  pre_setup       - Pre-setup tasks for Kafka Connect"
	@echo "  post_setup      - Post-setup tasks for Kafka Connect"
	@echo "  clean           - Clean up docker volumes"

# runs a kafka connect server for vehicle stream analytics
run_servers:
	@echo "Starting Kafka Connect servers..."
	@docker compose up -d

# shows list of kafka connect servers running in docker
show_servers:
	@echo "Listing running Kafka Connect servers..."
	@docker compose ps | grep connect
	@echo "Listing running minio servers..."
	@docker compose ps | grep minio

# stops kafka connect server and removes containers
stop_servers:
	@echo "Stopping Kafka Connect servers..."
	@docker compose down

# shows logs of kafka connect server
show_connect_logs:
	@echo "Showing Kafka Connect server logs..."
	@docker compose logs -f connect

# shows logs of minio server
show_minio_logs:
	@echo "Showing MinIO server logs..."
	@docker compose logs -f minio

# sets the topics needed for Kafka Connect
pre_setup:
	@echo "Pre-setup tasks for Kafka Connect..."
	@bash scripts/create-kafka-topics.sh

# registers the connectors for source and sink topics
post_setup:
	@echo "Post-setup tasks for Kafka Connect..."
	@bash scripts/register-connectors.sh

# safely cleans the kafka connect volume used for resetting
clean:
	@echo "Cleaning up docker volumes..."
	@bash scripts/clean.sh

.PHONY: help run_servers show_servers stop_servers show_connect_logs show_minio_logs pre_setup post_setup clean