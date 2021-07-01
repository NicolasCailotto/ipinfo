# Api IP Info
Api Rest para obtener informacion de una IP especifica, tambien permita agregar IPs a una lista de baneadas las cuales no podran consultar la informacion

## Endpoints disponibles
* GET http://{host}:{puerto}/v1/api/ip/{ipAddress}
<br> Este request devolver la informacion de la IP solicitada. </br>
* POST http://{host}:{puerto}/v1/api/ip
<br> Este metodo recibe una IP en el request body y la guarda como una IP bloqueada no permitiendole consultar la informacion.</br>
* GET http://{host}:{puerto}/swagger-ui.html
<br> Disponibilizacion de la documentacion para cada endpoint. Se muestra informacion de los request esperados, y sus posibles respuestas.
Ademas es posible utilizar los endpoint desde la misma web.</br>

<br> {host} y {puerto} se tiene que reemplazar por el host y puerto donde esta disponible la Api </br>
<br> {ipAddress} debe ser reemplazado por la direccion IP de la cual se quiere obtener la informacion </br>
<br> En el caso de request POST se espera en el body un json del siguiente formato </br>
<code> { "ipAddress": "172.0.0.1" } </code><br>


## Tecnologias usadas
* Spring Boot con Java 8
* Mongo como DB para persistencia de datos
* Redis para caching distribuido

# Ejecución en entorno local:

* Requerimientos previos
1) Tener instalado java 1.8.
2) Tener maven 3 instalado.
3) Para ejecutar el sistema en un entorno local primero deberá instalarse una instancia de MongoDB activa en el puerto tcp 27017 y una instancia de Redis activa en el puerto 6379. Ambos son los puertos por default

* Ejecución de la api
1) Iniciar jar
<code>java -jar target/ipinfo-0.0.1-SNAPSHOT.jar </code>
<br>la API quedará expuesta en http://localhost:8081/

# Ejecución con Docker:

* Requerimientos previos
1) Tener instalado Docker.
2) Tener maven 3 instalado.
3) Para ejecutar el sistema en un entorno con Docker primero deberá instalarse una instancia de MongoDB activa en el puerto tcp 27017 y una instancia de Redis activa en el puerto 6379. Ambos son los puertos por default
4) Modificar en el archivo config/application-docker.properties las properties 'spring.data.mongodb.uri' y 'spring.redis.host' para que apunten a localhost. Esto se debe hacer porque el archivo esta configurado para ejecutarse con docker-compose 

* Ejecución de la api
1) Compilar la aplicacion 
<code>mvn clean package</code>

2) Buildear imagen de docker 
<code>docker build -t ip-info-image . </code>

3) Correr un contenedor con la imagen previamente creada
<code>docker run -p 8080:8080 --name ip-info-container ip-info-image </code>
<br>la API quedará expuesta en http://localhost:8080/


# Ejecución con Docker Compose:

* Requerimientos previos
1) Tener instalado Docker.
2) Tener maven 3 instalado.
3) Tener instalado docker-compose.
4) Con esta opcion no es necesario tener instalado localmente Redis ni MongoDB porque van a estar disponibles como contenedores docker. 

* Ejecución de la api
1) Compilar la aplicacion 
<code>mvn clean package</code>

2) Correr contenedores con docker-compose
<code>docker-compose -f docker-compose.yaml up </code>
<br>El comando levantara 4 contenedores. Uno para MongoDB, otro para Redis, otro para Mongo-Express y por ultimo la API que estará disponible en http://localhost:8080/


# Mejoras a realizar - Consideraciones:

* Rest Template: Para las consultas a APIs externas se utiliza RestTemplate con su configuracion por default, esto no es lo ideal porque en su funcionamiento interno cada vez que se desea hacer un request se crea una nueva conexión
lo que es bastante costoso sobre todo cuando es posible que se reciba mucho trafico. Lo ideal es configurar RestTemplate para que maneje un pool de conexiones, esto fue realizado en un branch aparte 'rest-tempĺate-pooling'. Se configuro un pool, junto con parametros de timeout y conecciones maximas para host especificos, adicionalmente se configura un proceso 
que corre cada cierta cantidad de tiempo que limpias las conecciones viejas.
* Open Feign: En vez de utilizar Rest Template para las llamadas Http tambien es posible utilizar Open Feign para reducir el codigo.  
* Secrets: Existen APIs externas a las que es necesario enviarlo un access_key en los request. Estos estan disponibles desde las properties lo cual no es lo ideal y deberian ser tomadas de un Vault en caso de que la aplicacion sea utilizada con un orquestador de contenedores como Kubernetes.
Opcionalmente tambien se podira configurar el Dockerfile para setear estas keys en variables de entorno y que Spring tome dichas properties desde ahi.     
* Servicios Health: La aplicacion tambien tiene disponible un endpoint de health check, es util cuando se usa orquestacion de contenedores y configuracion de HealthCheckProbe 
* Seguridad: En esta version de la aplicacion no se dispone de seguridad, mas halla de las ips baneadas, en un entorno real se deberia configurar seguridad oAuth o custom.
* Caché: Actualmente para reducir las consultas a las apis externas se utilizo una cache distribuida (Redis) que tiene configuraciones de TTL (CacheConfig.java) esto permite que si se reciben varios request iguales la api no tenga que volver a ejecutar todos los request. 