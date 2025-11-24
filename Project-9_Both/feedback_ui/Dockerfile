
#------------------BUILD STAGE

# Identify the runtime the app needs
FROM node:20-slim AS build

# app can be more explicit but this is general convention 
WORKDIR /app


# this comes before npm install so docker only re-runs if the package.json changes
COPY package*.json ./

RUN npm install

# first dot means “from my local working folder”. second dot means “into the current WORKDIR (which is /app) inside the container” 
COPY . .

RUN npm run build

#------------RUN STAGE

FROM node:20-slim

WORKDIR /app

# copies only the built dist folder into the runtime container
# keeps the runtime image extremely small
COPY --from=build /app/dist ./dist

# install static file server(serve) 
# lets us run: -s dist, serve static files from /app/dist and
# -l 5173, to listen on port 5173
RUN npm install -g serve

EXPOSE 5173

# launches a small static file server and serve the dist folder.
CMD ["serve", "-s", "dist", "-l", "5173"]


# ----------TESTING COMMANDS
#  docker build -t feedback-ui .  : to build app
#  docker run -p 5173:5173 feedback-ui  : starts the container that was just built
#  http://localhost:5173  : check and see if application runs in the browser

