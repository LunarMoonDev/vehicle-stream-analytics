default: help

help:
	@echo "Makefile for managing Schema Registry servers"
	@echo "Available commands:"
	@echo "  run_servers     - Start Schema Registry servers"
	@echo "  show_servers    - List running Schema Registry servers"
	@echo "  stop_servers    - Stop Schema Registry servers and remove containers"
	@echo "  show_logs       - Show logs of the Schema Registry server"
	@echo "  pre_setup       - Pre-setup tasks for Schema Registry"
	@echo "  post_setup      - Post-setup tasks for Schema Registry"
	@echo "  clean           - Clean up docker volumes"
	
# runs a schema registry server for vehicle stream analytics
run_servers:
	@echo "Starting Schema Registry servers..."
	@docker compose up -d

# shows list of schema registry servers running in docker
show_servers:
	@echo "Listing running Schema Registry servers..."
	@docker compose ps | grep schema_registry

# stops schema registry server and removes containers
stop_servers:
	@echo "Stopping Schema Registry servers..."
	@docker compose down

# shows logs of schema registry server
show_logs:
	@echo "Showing Schema Registry server logs..."
	@docker compose logs -f schema_registry

# sets up the schema registry with necessary topics
pre_setup:
	@echo "Pre-setup tasks for Schema Registry..."
	@bash scripts/create-kafka-topics.sh

# registers the schema for the vehicle stream
post_setup:
	@echo "Post-setup tasks for Schema Registry..."
	@bash scripts/register-schema.sh

.PHONY: help run_servers show_servers stop_servers show_logs pre_setup post_setup clean