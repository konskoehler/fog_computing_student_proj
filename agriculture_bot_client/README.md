## About this Project
This project consists of a server and at least one (edge) client. In order to comply with edge specifications for edge 
devices the clients will need to cache and buffer (sensor) data and messages. The data is persisted at the client side
using a local mongoDB server instance running in a docker container.

On the server side a connection to a distributed mongoDB cluster is used to save data.

##Getting Started:

### Create local docker mongoDB instance
Start mongo server instance:
```
sudo docker run -d --network host --name aggriculture-client-mongo -e MONGO_INITDB_ROOT_USERNAME=aggriculture_bot -e MONGO_INITDB_ROOT_PASSWORD=iloveplants mongo
```
Connect to mongo instance from another container
```
sudo docker run -it --rm --network host mongo \
mongo 127.0.0.1 \
-u aggriculture_bot \
-p iloveplants \
--authenticationDatabase admin
```

Now you can create a new database:
```
>use aggriculture_fog_project_db
```

And create a new collection
```
>db.createCollection("missionCollection")
```

### Set Environment Variable
In order to connect to the respective mongo database on the Server and Client the environment variable "FOGMONGODATABASE"
needs to be set to the correct uri
```
export FOGMONGODATABASE=<uri>
```

## Start Server and Client
Make sure there is mongoDB instance running locally. This instance is only used by the client.
```
docker start aggriculture-client-mongo
```

Start Server.kt and Client.kt or run respective jar files.









