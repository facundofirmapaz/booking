# **Resolución del challenge**

**Items 1 y 2 se resolvieron en un solo request:**

Get que recibe 3 parameters opcionales o bien ninguno. Para el caso que no existan parametros devuelve todos los hoteles.
Según los parameters ingresados comprueba las siguientes condiciones:

Destino: Tiene que ser destino válido.

DateFrom: Fecha desde la cual se espera tener una habitación libre.

DateTo: Fecha hasta la cual se espera tener una habitación libre.

**Items 3 se resolvió en un solo request:**

Post que en su body lleva la información de un booking.
Se modela con el dto NewReserv, el mismo es validado y luego dado de alta.
Para darlo de alta se lleva el registro en memoria del ReservDto generado.
Esto simula el principio de una "tabla" la cual llevaría el registro de las reservas realizadas, dejando
sin modificar la tabla de información de habitaciones desacoplada de su estado.

**Items 4 y 5 se resolvió en un solo request:**

Get muy similar al de los items 1 y 2. En este caso hay un filtro más que es el origen.
Se decidió modelar todas las validaciones en comun con un solo servicio el cual se inyecta en HotelService y FlightService.

**Para los request resueltos se agrega una collection de PostMan para su ejecución. Ademas de una captura con los porcentajes de cobertura de código.**



_DISCLAIMER_

_El item 6 del challenge (POST para reservar un vuelo) no se resolvio debido al tiempo con el cual se contaba, en lugar de atacar dicho item
se optó por enfocarse más en los test unitarios (tema principal del challenge)._

