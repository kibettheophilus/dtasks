#!/bin/bash

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${GREEN}Starting JSON Server with ngrok setup...${NC}\n"

# Check if json-server is installed
echo "Checking for json-server..."
if ! command -v json-server &> /dev/null; then
    echo -e "${YELLOW}json-server not found. Installing globally...${NC}"
    npm install -g json-server
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}json-server installed successfully!${NC}\n"
    else
        echo -e "${RED}Failed to install json-server. Please check npm permissions.${NC}"
        exit 1
    fi
else
    echo -e "${GREEN}json-server is already installed.${NC}\n"
fi

# Check if db directory exists
if [ ! -d "db" ]; then
    echo -e "${RED}Error: 'db' directory not found!${NC}"
    exit 1
fi

# Check if db.json exists
if [ ! -f "db/db.json" ]; then
    echo -e "${RED}Error: 'db/db.json' file not found!${NC}"
    exit 1
fi

# Check if ngrok is installed
echo "Checking for ngrok..."
if ! command -v ngrok &> /dev/null; then
    echo -e "${YELLOW}ngrok not found. Installing...${NC}"

    # Detect OS
    OS=$(uname -s)
    case "$OS" in
        Linux*)
            # Linux installation
            if command -v apt-get &> /dev/null; then
                # Debian/Ubuntu
                curl -s https://ngrok-agent.s3.amazonaws.com/ngrok.asc | sudo tee /etc/apt/trusted.gpg.d/ngrok.asc >/dev/null
                echo "deb https://ngrok-agent.s3.amazonaws.com buster main" | sudo tee /etc/apt/sources.list.d/ngrok.list
                sudo apt update && sudo apt install ngrok
            elif command -v yum &> /dev/null; then
                # RedHat/CentOS
                sudo yum install -y ngrok
            else
                # Generic Linux - download binary
                wget https://bin.equinox.io/c/bNyj1mQVY4c/ngrok-v3-stable-linux-amd64.tgz
                tar xvzf ngrok-v3-stable-linux-amd64.tgz
                sudo mv ngrok /usr/local/bin/
                rm ngrok-v3-stable-linux-amd64.tgz
            fi
            ;;
        Darwin*)
            # macOS installation
            if command -v brew &> /dev/null; then
                brew install ngrok/ngrok/ngrok
            else
                echo -e "${RED}Homebrew not found. Please install Homebrew first or download ngrok manually from https://ngrok.com/download${NC}"
                exit 1
            fi
            ;;
        MINGW*|MSYS*|CYGWIN*)
            # Windows (Git Bash/MSYS/Cygwin)
            echo -e "${YELLOW}Please download ngrok manually from https://ngrok.com/download for Windows${NC}"
            exit 1
            ;;
        *)
            echo -e "${RED}Unsupported OS: $OS${NC}"
            exit 1
            ;;
    esac

    if [ $? -eq 0 ]; then
        echo -e "${GREEN}ngrok installed successfully!${NC}\n"
    else
        echo -e "${RED}Failed to install ngrok.${NC}"
        exit 1
    fi
else
    echo -e "${GREEN}ngrok is already installed.${NC}\n"
fi

# Start json-server in background
echo -e "${GREEN}Starting json-server on port 3000...${NC}"
cd db
json-server --watch db.json --port 3000 &
JSON_SERVER_PID=$!
cd ..

# Wait a moment for json-server to start
sleep 3

# Check if json-server started successfully
if ps -p $JSON_SERVER_PID > /dev/null; then
    echo -e "${GREEN}json-server started successfully (PID: $JSON_SERVER_PID)${NC}\n"
else
    echo -e "${RED}Failed to start json-server${NC}"
    exit 1
fi

# Start ngrok in background
echo -e "${GREEN}Starting ngrok tunnel...${NC}"
ngrok http 3000 > /dev/null &
NGROK_PID=$!

# Wait for ngrok to initialize
sleep 3

# Get the ngrok URL
echo -e "${GREEN}Fetching ngrok URL...${NC}\n"
NGROK_URL=$(curl -s http://localhost:4040/api/tunnels | grep -o '"public_url":"https://[^"]*' | grep -o 'https://[^"]*' | head -n 1)

if [ -z "$NGROK_URL" ]; then
    echo -e "${RED}Failed to retrieve ngrok URL${NC}"
    kill $JSON_SERVER_PID $NGROK_PID 2>/dev/null
    exit 1
fi

# Print the URL in a nice format
echo -e "${GREEN}╔════════════════════════════════════════════════════════════╗${NC}"
echo -e "${GREEN}║${NC}  ${YELLOW}Your ngrok URL is:${NC}                                      ${GREEN}║${NC}"
echo -e "${GREEN}║${NC}  ${NGROK_URL}  ${GREEN}║${NC}"
echo -e "${GREEN}╔════════════════════════════════════════════════════════════╗${NC}"
echo -e "\n${YELLOW}Press Ctrl+C to stop both services${NC}\n"

# Keep script running
wait $NGROK_PID

# Cleanup when script exits
trap "kill $JSON_SERVER_PID $NGROK_PID 2>/dev/null" EXIT