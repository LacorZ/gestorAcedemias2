docker-compose -f .\src\main\docker\app.yml  down
.\generarBBDDconJDL.ps1
docker-compose -f .\src\main\docker\app.yml  up