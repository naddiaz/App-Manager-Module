# BLE Tasker
__Task assignment through indoor location with bluetooth low energy devices__

___Keywords:___ Location-Based Applications and Services, Secure communication, Bluetooth Low Energy.

## Resumen
Este trabajo combina el uso de teléfonos inteligentes con dispositivos Bluetooth Low Energy con el propósito de garantizar un servicio eficiente de comunicación en instalaciones interiores. La combinación de estas dos herramientas permite crear un innovador sistema de control que provee de un canal de comunicación más eficiente y seguro entre la compañía y sus trabajadores. Además también se ha dotado al sistema de la capacidad de optimizar la asignación de recursos a las tareas que se llevan a cabo por los empleados en grandes complejos, como aeropuertos.

Esto es parte de un trabajo en proceso, que incluye el desarrollo del sistema propuesto en un aeropuerto para optimizar las operaciones en tierra dentro del aeropuerto. Para implementar este sistema se hace uso de una plataforma, la cual es el núcleo de toda la actividad que se realiza dentro de la instalación. También es necesaria la existencia de un receptor para esta comunicación, por lo que se ha desarrollado una aplicación Android que será accesible por los empleados de dicha instalación.

## Abstract
This work combines smartphones with Bluetooth Low Energy devices with the purpose of guaranteeing an efficient communication service in indoor installations. The combination of these two tools allows creating an innovative control unit to provide a more efficient and secure communication channel between a company and its employees. Besides, the system has been also endowed with the ability to optimize the assignment of the tasks that are carried out in large complexes like airports.

This part of a work in progress, which includes the deployment of the proposed system in an airport in order to optimize ground operations inside the airport. To implement this system using a web platform, which is the core of all activity that perform to into installation. Also is needed the existence of a receptor for this communication, so has develop an Android application to be accessible by employees of the installation.  

## Las balizas (UriBeacons)
Para este trabajo se ha utilizado como elemento principal en la localización de los empleados en el interior de la instalación la tecnología __Bluetooth Low Energy__ (BLE). Esta tecnología nos permite marcar puntos de referencia a lo largo de la instalación, de tal manera que podamos asociar a cada empleado a un punto determinado de la misma.

En concreto se ha hecho uso de las nuevas balizas __The physical web__ de __Google__. Estos dispositivos tienen un estándar diferente y permiten no sólo emitir una señal y proporcionar un __UUID__ sino que son capaces de almacenar una URL. En concreto para este proyecto no se ha utilizado todo el potencial que nos ofrecen pero a lo largo del desarrollo de este trabajo ha dado lugar a una librería que está en desarrollo para el manejo completo de las balizas.

## Beacons
For this work has been used as main element in locating system the Bluetooth Low Energy technology. This technology allows us to set references points into installation and we can to associate each employee in certain point of it.

In particular, we use __The physical web__ beacons offered by __Google__. These devices have a different standard to allow not only emit a signal and provide a __UUID__ but can store a URL. Specifically for this project has not used the full potential but we begin to develop a library to manage these devices.

### Official Website
[The Physical Web](https://google.github.io/physical-web/)
