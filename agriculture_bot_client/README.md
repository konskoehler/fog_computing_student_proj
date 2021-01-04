Install CoreOS rkt
gpg --recv-key 18AD5014C99EF7E3BA5F6CE950BDD3E0FC8A365E
wget https://github.com/rkt/rkt/releases/download/v1.29.0/rkt_1.29.0-1_amd64.deb
wget https://github.com/rkt/rkt/releases/download/v1.29.0/rkt_1.29.0-1_amd64.deb.asc
gpg --verify rkt_1.29.0-1_amd64.deb.asc
sudo dpkg -i rkt_1.29.0-1_amd64.deb



## Start Server and Client
Server and Client need to get started with the respective mongoDB URL set as environmental variable "FOGMONGODATABASE".
export FOGMONGODATABASE=


docker network create agriculture-network


sudo docker run -d --network agriculture-network --name aggriculture-client-mongo -e MONGO_INITDB_ROOT_USERNAME=aggriculture_bot -e MONGO_INITDB_ROOT_PASSWORD=iloveplants mongo

docker run -it --rm --network agriculture-network mongo \
    mongo --host aggriculture-client-mongo \
        -u aggriculture_bot \
        -p iloveplants \
        --authenticationDatabase admin \
        aggriculture_fog_project_db

Check if database was created successfully:
'''
> db.getName()
aggriculture_fog_project_db
'''

Create collection
'''>db.createCollection("missionCollection")'''
