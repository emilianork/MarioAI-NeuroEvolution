Ya lo deje listo para que compile con ant.

$ ant compile

o solamente con

$ ant

Para correr el programa solamente usa el siguiente comando:

$ java -classpath '.:lib/*' ch.idsia.scenarios.Main

dentro de la carpeta bin que genero el compilador.

Ahora para que comiences a ver mas o menos como funciona todo el programa (por que desgraciadamente
no comentaron casi nada); mete a la pagina:

http://www.marioai.org/gameplay-track/getting-started

Ahi viene lo basico, recuerda todos los comandos que vienen en esa pagina sirven tal cual 
en el que compila ant, simplemente para correrlos agrega la parte de -classpath '.:lib/*' 
entre java y ch.

En la pagina dice que podemos usar muchos otros lenguajes de programacion para hacer los
agentes; quite todos los ejemplos de esto, por que es mas facil hacierlo en java, que lo
compile ant y correrlo con los comandos.

Mi recomendacion es que intentes primero hacer un agente que haga algo super estupido
como ir adelante o algo asi.
Y despues pienso que abra que hacer otro programa que mande a llamaer varias veces el juego
con nuestro agente, y con un escenario random(segun recuerdo viene el comando en internet
para generar los escenarios random), para que guarde la red neuronal en un archivo y cada vez
que se ejecuta mario lea el estado en que lo dejo el mario anterior.