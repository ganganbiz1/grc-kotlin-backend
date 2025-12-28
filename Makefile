.PHONY: help up up-build down restart logs ps build clean db app test

# Default target
help:
	@echo "Usage: make [target]"
	@echo ""
	@echo "Docker Compose:"
	@echo "  up        - Start all services (app + db)"
	@echo "  up-build  - Start all services with rebuild"
	@echo "  down      - Stop all services"
	@echo "  restart   - Restart all services"
	@echo "  logs      - Show logs (follow mode)"
	@echo "  ps        - Show running containers"
	@echo ""
	@echo "Development:"
	@echo "  db        - Start only database"
	@echo "  app       - Run app with Gradle (requires db)"
	@echo "  build     - Build the application"
	@echo "  test      - Run tests"
	@echo "  clean     - Clean build artifacts"

# Docker Compose commands
up:
	docker compose up -d

up-build:
	docker compose up -d --build

down:
	docker compose down

restart:
	docker compose restart

logs:
	docker compose logs -f

ps:
	docker compose ps

# Development commands
db:
	docker compose up -d db

app:
	./gradlew bootRun --args='--spring.profiles.active=local'

build:
	./gradlew build

test:
	./gradlew test

clean:
	./gradlew clean
