DEFAULT: jar

# ============= BUILDS =============
jar: ## build executable jar
	@./scripts/build.sh

test: ## execute tests
	@./scripts/test.sh

docker: jar ## build docker image
	@./scripts/docker.sh

help: ## print this help
	@awk 'BEGIN {FS = ":.*?## "} /^[a-zA-Z0-9_-]+:.*?## / {gsub("\\\\n",sprintf("\n%22c",""), $$2);printf "\033[36m%-20s\033[0m %s\n", $$1, $$2}' $(MAKEFILE_LIST)