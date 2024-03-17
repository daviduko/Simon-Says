# Simon-Says

Este proyecto es una recreación del Simon Says en Android Studio.
En este caso, para dibujar los botones utilizo Canvas, dibujando cuatro circulos de distintos colores.
Para detectar si se ha clickad un botón, uso la función heredada de View: onTouchEvent() y miro en que cuadrante de la view está el click (no es lo mejor porque se puede clickar fura del circulo y lo dará por bueno).
Para la secuencia, llamo a una función recursiva showNextColor() que se llama hasta que ha enseñado todos los colores de la secuancia
