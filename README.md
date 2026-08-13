# Parser XML — Árbol Multihijos

**Materia**: Estructura de Datos | **Periodo**: 2025-2 | **Estado**: Completado

## Equipo de trabajo

- [Daniel Vaca Velastegui](https://github.com/DanielV-13)
- [Xavier Cárdenas](https://github.com/Xavier2806)

Analizador (parser) de archivos XML implementado desde cero en Java, sin librerías XML externas. Construye un árbol multihijos a partir del documento, valida su estructura y permite recorrerlo, buscar, ordenar y generar archivos a través de una interfaz gráfica Swing.

## Capturas / Demo

![Vista principal](docs/screenshots/main.png)

> Demo en vivo: _(no aplica — aplicación de escritorio)_ | Video/GIF: _[opcional]_

## Funcionalidad

- [x] Lectura y validación de archivos XML sin librerías externas [dfd6469](https://github.com/DanielV-13/ESTRUCTURA-DE-DATOS-PROYECTO-PARSER-XML/commit/dfd6469)
- [x] Construcción y recorridos del árbol multihijos (clases `ArbolXML` y `NodoXML`) [0815ca7](https://github.com/DanielV-13/ESTRUCTURA-DE-DATOS-PROYECTO-PARSER-XML/commit/0815ca7)
- [x] Búsqueda de valores ordenados mediante `Heap` [88db525](https://github.com/DanielV-13/ESTRUCTURA-DE-DATOS-PROYECTO-PARSER-XML/commit/88db525)
- [x] Interfaz gráfica con paneles de carga, recorridos, búsqueda, ordenamiento y agregar nodo [3bbbd2e](https://github.com/DanielV-13/ESTRUCTURA-DE-DATOS-PROYECTO-PARSER-XML/commit/3bbbd2e)
- [x] Generación de archivos XML a partir del árbol [3b4bbcf](https://github.com/DanielV-13/ESTRUCTURA-DE-DATOS-PROYECTO-PARSER-XML/commit/3b4bbcf)

## Tecnologías

`Java` | `Swing (GUI)` | `Sin base de datos` | `IntelliJ IDEA`

Estructuras de datos aplicadas: árbol multihijos, pila (`Stack`), `Heap`, `HashMap` y `ArrayList`.

## Ejecución

```bash
# Instrucciones paso a paso
git clone https://github.com/DanielV-13/ESTRUCTURA-DE-DATOS-PROYECTO-PARSER-XML.git
cd ESTRUCTURA-DE-DATOS-PROYECTO-PARSER-XML

# Compilar
javac -d out src/*.java

# Ejecutar
java -cp out MainGUI
```

También puede abrirse directamente en IntelliJ IDEA y ejecutar la clase `MainGUI`.

## Métricas de Progreso

| Indicador | Valor |
|---|---|
| Commits totales | 11 |
| Issues/PRs fusionados | 1 |
| Cobertura de pruebas | N/A |
| Última actualización | 2025-12-20 |

## Reflexión y Aprendizajes

- **Habilidad desarrollada**: Implementación de estructuras de datos no lineales (árboles multihijos) y su recorrido, manejo de pilas para el parsing y uso de un Heap para ordenamiento, todo aplicado a un problema real de análisis de documentos XML.
- **Qué funcionó bien**: Separar la lógica en clases con responsabilidad única (`NodoXML` para el modelo, `ArbolXML` para las operaciones, `Heap` para el ordenamiento y `VentanaXML` para la interfaz) hizo el código mantenible y fácil de extender con nuevos paneles.
- **Qué se podría mejorar**: Agregar pruebas unitarias sobre el parser para cubrir casos borde de XML mal formado, y desacoplar aún más la lógica de la interfaz gráfica para poder reutilizar el motor de parsing sin Swing.
- **Conceptos clave aplicados de la materia**: Árboles y recorridos, pilas (LIFO), colas de prioridad / Heap, complejidad algorítmica y encapsulamiento de estructuras.
