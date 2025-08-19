default: help

help:
	@echo "Makefile for managing Ingest servers"
	@echo "Available commands:"
	@echo "  run_servers     - Start Ingest servers"
	@echo "  show_servers    - List running Ingest servers"
	@echo "  stop_servers    - Stop Ingest servers and remove containers"
	@echo "  show_logs       - Show logs of the Ingest server"
	@echo "  clean           - Clean up docker volumes"
	
# runs a kafka stream server for json to avro conversion
run_servers:
	@echo "Starting Ingest servers..."
	@docker compose up -d

# shows list of ingest servers running in docker
show_servers:
	@echo "Listing running Ingest servers..."
	@docker compose ps | grep json_to_avro

# shows logs of ingest server
show_logs:
	@echo "Showing Ingest server logs..."
	@docker compose logs -f json_to_avro

# stops ingest server and removes containers
stop_servers:
	@echo "Stopping Ingest servers..."
	@docker compose down

# shows logs of json to avro ingest server
show_logs:
	@echo "Showing Ingest server logs..."
	@docker compose logs -f json_to_avro

.PHONY: help run_servers show_servers stop_servers show_logs clean