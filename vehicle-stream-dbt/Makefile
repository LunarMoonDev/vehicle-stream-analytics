default: help

help:
	@echo "Makefile for managing Database servers"
	@echo "Available commands:"
	@echo "  run_servers     - Start Database servers"
	@echo "  show_servers    - List running Database servers"
	@echo "  stop_servers    - Stop Database servers and remove containers"
	@echo "  show_logs       - Show logs of the Database server"
	@echo "  clean           - Clean up docker volumes"
	
# runs postgres server
run_servers:
	@echo "Starting Database servers..."
	@docker compose up -d

# shows list of postgres server running in docker
show_servers:
	@echo "Listing running Database servers..."
	@docker compose ps | grep postgres

# stops postgres server and removes container
stop_servers:
	@echo "Stopping Database servers..."
	@docker compose down

# shows logs of postgres server
show_logs:
	@echo "Showing Database server logs..."
	@docker compose logs -f postgres

# safely cleans the postgres volume used for resetting
clean:
	@echo "Cleaning up docker volumes..."
	@bash scripts/clean.sh

.PHONY: help run_servers show_servers stop_servers show_logs clean