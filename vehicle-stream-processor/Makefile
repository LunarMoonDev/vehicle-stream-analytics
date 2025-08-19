default: help

help:
	@echo "Makefile for managing Stream Processor servers"
	@echo "Available commands:"
	@echo "  run_servers     - Start Stream Processor servers"
	@echo "  show_servers    - List running Stream Processor servers"
	@echo "  stop_servers    - Stop Stream Processor servers and remove containers"
	@echo "  show_logs       - Show logs of the Stream Processor server"
	@echo "  clean           - Clean up docker volumes"

# runs a stream processor server for vehicle stream analytics
run_servers:
	@echo "Starting Processor servers..."
	@docker compose up -d

# shows list of stream processor servers running in docker
show_servers:
	@echo "Listing running Processor servers..."
	@docker compose ps | grep stream_processor

# shows logs of stream processor server
show_logs:
	@echo "Showing Processor server logs..."
	@docker compose logs -f stream_processor

# stops stream processor server and removes containers
stop_servers:
	@echo "Stopping Processor servers..."
	@docker compose down

# shows logs of stream processor server
show_logs:
	@echo "Showing Processor server logs..."
	@docker compose logs -f stream_processor

.PHONY: help run_servers show_servers stop_servers show_logs clean