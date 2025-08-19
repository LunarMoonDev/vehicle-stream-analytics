default: help

help:
	@echo "Makefile for managing Kafka servers"
	@echo "Available commands:"
	@echo "  run_servers     - Start Kafka servers"
	@echo "  show_servers    - List running Kafka servers"
	@echo "  stop_servers    - Stop Kafka servers and remove containers"
	@echo "  show_server1_logs - Show logs of broker1 Kafka server"
	@echo "  show_server2_logs - Show logs of broker2 Kafka server"
	@echo "  clean           - Clean up docker volumes"

# runs kafka server with 2 brokers
run_servers:
	@echo "Starting Kafka servers..."
	@docker compose up -d

# shows list of kafka servers running in docker
show_servers:
	@echo "Listing running Kafka servers..."
	@docker compose ps | grep kafka

# stops kafka server and removes containers
stop_servers:
	@echo "Stopping Kafka servers..."
	@docker compose down

# shows logs of broker1 kafka
show_server1_logs:
	@echo "Showing Kafka server logs..."
	@docker compose logs -f kafka

# shows logs of broker2 kafka
show_server2_logs:
	@echo "Showing Kafka Connect server logs..."
	@docker compose logs -f kafka1

# safely cleans the kafka volume used for resetting
clean:
	@echo "Cleaning up docker volumes..."
	@bash scripts/clean.sh

.PHONY: help run_servers show_servers stop_servers show_server1_logs show_server2_logs clean