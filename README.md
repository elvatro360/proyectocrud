# 📊 CRUD: Gestiona Datos Completos

> Aprende todas las operaciones de Base de Datos: Crear, Leer, Actualizar, Eliminar

---

## 📑 Índice Rápido

- [¿Qué es CRUD?](#qué-es-crud)
- [Las 4 Operaciones](#las-4-operaciones)
- [Instala el Proyecto](#instala-el-proyecto)
- [Entiende el Código](#entiende-el-código)
- [Experimenta](#experimenta)
- [Desafíos](#desafíos)

---

## ❓ ¿Qué es CRUD?

### El Acrónimo

```
C - CREATE  (Crear)    ➕ Agregar nuevo dato
R - READ    (Leer)     👁️ Ver datos
U - UPDATE  (Actualizar) ✏️ Modificar dato
D - DELETE  (Eliminar)  🗑️ Borrar dato
```

### Ejemplos del Mundo Real

```
Netflix (Videos):
  C = Agregar película nueva
  R = Ver películas
  U = Cambiar descripción
  D = Eliminar película

Instagram (Posts):
  C = Crear post nuevo
  R = Ver feed
  U = Editar caption
  D = Eliminar post

Gmail (Emails):
  C = Escribir email
  R = Leer emails
  U = Cambiar etiqueta
  D = Borrar email
```

### ¿Por Qué es Importante?

```
CRUD es 80% de aplicaciones web:

✓ Blog = CRUD de artículos
✓ E-commerce = CRUD de productos
✓ Red social = CRUD de posts
✓ Banco = CRUD de transacciones
✓ Hospital = CRUD de pacientes
```

---

## 🔄 Las 4 Operaciones

### 1️⃣ CREATE (Crear)

```python
# Agregamos un producto nuevo
Producto.objects.create(
    nombre="Laptop",
    descripcion="Laptop gaming",
    precio=1500.00
)

En base de datos:
├─ ID: 1
├─ Nombre: Laptop
├─ Descripción: Laptop gaming
└─ Precio: 1500.00
```

### 2️⃣ READ (Leer)

```python
# Obtenemos todos
todos = Producto.objects.all()

# Obtenemos uno específico
uno = Producto.objects.get(id=1)

# Filtramos
baratos = Producto.objects.filter(precio__lt=100)

En pantalla:
├─ Laptop: $1500.00
├─ Mouse: $25.00
└─ Teclado: $75.00
```

### 3️⃣ UPDATE (Actualizar)

```python
# Obtener y modificar
producto = Producto.objects.get(id=1)
producto.precio = 1200.00  # Cambio el precio
producto.save()            # Guardo cambios

O directo:
Producto.objects.filter(id=1).update(precio=1200.00)

En base de datos:
├─ ID: 1
├─ Nombre: Laptop
├─ Descripción: Laptop gaming
└─ Precio: 1200.00 (¡actualizado!)
```

### 4️⃣ DELETE (Eliminar)

```python
# Obtener y eliminar
producto = Producto.objects.get(id=1)
producto.delete()

O directo:
Producto.objects.filter(id=1).delete()

En base de datos:
└─ (ID 1 no existe)
```

---

## 🚀 Instala el Proyecto

### Estructura de Carpetas

```
proyecto_crud/
├── manage.py
├── requirements.txt
├── mysite/
│   ├── settings.py
│   ├── urls.py
│   └── wsgi.py
├── cruDapp/
│   ├── models.py      ← Defines Producto
│   ├── views.py       ← Lógica CRUD
│   ├── urls.py        ← Rutas CRUD
│   └── templates/
│       ├── lista.html       ← Ver todos
│       ├── crear.html       ← Crear nuevo
│       ├── editar.html      ← Modificar
│       └── detalle.html     ← Ver uno
├── static/            ← CSS, JS
└── venv/              ← Ambiente virtual
```

### Instalación

```bash
# 1. Instalar dependencias
pip install -r requirements.txt

# 2. Hacer migraciones
python manage.py makemigrations
python manage.py migrate

# 3. Crear admin
python manage.py createsuperuser

# 4. Ejecutar
python manage.py runserver
```

---

## 💻 Entiende el Código

### Modelo (models.py)

```python
from django.db import models

class Producto(models.Model):
    nombre = models.CharField(max_length=100)
    descripcion = models.TextField()
    precio = models.DecimalField(max_digits=10, decimal_places=2)
    fecha_creacion = models.DateTimeField(auto_now_add=True)
    
    def __str__(self):
        return self.nombre
```

**Qué significa:**
- `CharField` = Texto corto (100 caracteres)
- `TextField` = Texto largo (paragrafos)
- `DecimalField` = Número decimal (precios)
- `DateTimeField` = Fecha y hora

### Vistas (views.py)

```python
from django.shortcuts import render, redirect, get_object_or_404
from .models import Producto

# READ: Ver todos
def lista_productos(request):
    productos = Producto.objects.all()
    return render(request, 'lista.html', {'productos': productos})

# CREATE: Crear nuevo
def crear_producto(request):
    if request.method == 'POST':
        nombre = request.POST['nombre']
        descripcion = request.POST['descripcion']
        precio = request.POST['precio']
        
        Producto.objects.create(
            nombre=nombre,
            descripcion=descripcion,
            precio=precio
        )
        return redirect('lista_productos')
    
    return render(request, 'crear.html')

# UPDATE: Editar
def editar_producto(request, id):
    producto = get_object_or_404(Producto, id=id)
    
    if request.method == 'POST':
        producto.nombre = request.POST['nombre']
        producto.descripcion = request.POST['descripcion']
        producto.precio = request.POST['precio']
        producto.save()
        return redirect('lista_productos')
    
    return render(request, 'editar.html', {'producto': producto})

# DELETE: Eliminar
def eliminar_producto(request, id):
    producto = get_object_or_404(Producto, id=id)
    
    if request.method == 'POST':
        producto.delete()
        return redirect('lista_productos')
    
    return render(request, 'confirmar_delete.html', {'producto': producto})
```

### Rutas (urls.py)

```python
from django.urls import path
from . import views

urlpatterns = [
    path('', views.lista_productos, name='lista_productos'),         # READ
    path('crear/', views.crear_producto, name='crear_producto'),     # CREATE
    path('editar/<int:id>/', views.editar_producto, name='editar'),  # UPDATE
    path('eliminar/<int:id>/', views.eliminar_producto, name='eliminar'),  # DELETE
]
```

---

## 🎮 Experimenta

### Ejercicio 1: Crear Producto

1. Abre: http://localhost:8000/crear/
2. Llena formulario:
   - Nombre: "Videojuego"
   - Descripción: "Juego aventura"
   - Precio: 59.99
3. Haz clic "Guardar"
4. ¡Se agregó a la BD! ✓

### Ejercicio 2: Ver Todos

1. Abre: http://localhost:8000/
2. Ves lista de productos
3. Cada uno muestra:
   - Nombre
   - Descripción
   - Precio

### Ejercicio 3: Editar Producto

1. En lista, haz clic "Editar" en un producto
2. Cambia el precio a $49.99
3. Guarda
4. Vuelve a lista: ¡precio actualizado! ✓

### Ejercicio 4: Eliminar Producto

1. En lista, haz clic "Eliminar"
2. Confirma que deseas eliminar
3. ¡Puff! Desapareció de la BD ✓

---

## 🎯 Desafíos

### Fácil ⭐
```
1. Agrega campo "categoria" a Producto
2. Filtra productos por categoría
3. Muestra en la lista
```

### Medio ⭐⭐
```
1. Agrega campo "stock" (cantidad)
2. Valida que stock > 0
3. Muestra "Agotado" si stock = 0
```

### Difícil ⭐⭐⭐
```
1. Agrega modelo "Venta"
2. Cada venta vincula productos
3. Reduce stock automáticamente
4. Crea reportes de ventas
```

---

## 🗄️ Base de Datos Detrás de Escenas

### Cómo Django Guarda

```
Antes de guardar: Python
producto = Producto.objects.create(nombre="Laptop", precio=1500)

Django convierte a SQL:
INSERT INTO cruDapp_producto (nombre, precio) 
VALUES ('Laptop', 1500);

En base de datos (SQLite):
┌────┬───────┬────────┐
│ ID │ Nombre│ Precio │
├────┼───────┼────────┤
│ 1  │ Laptop│  1500  │
└────┴───────┴────────┘
```

### Relaciones (Avanzado)

```python
# Un usuario puede tener muchas órdenes
class Usuario(models.Model):
    nombre = CharField()

class Orden(models.Model):
    usuario = ForeignKey(Usuario)  # Vinculación
    fecha = DateTimeField()

# Obtener órdenes de un usuario:
ordenes = Orden.objects.filter(usuario_id=1)
```

---

## 📚 Vocabulario

| Término | Significado |
|---------|------------|
| **Modelo** | Definición de datos (estructura) |
| **Instancia** | Un dato real (1 producto) |
| **QuerySet** | Lista de datos de BD |
| **Migración** | Cambio de estructura de BD |
| **ORM** | Convierte Python ↔️ SQL |
| **Foreign Key** | Vinculación entre tablas |
| **Admin** | Panel para gestionar datos |

---

## 🏁 Próximos Pasos

### Amplía tu Conocimiento

```
1. Relaciones más complejas
   ├─ Muchos a muchos
   ├─ Herencia de modelos
   └─ Consultas avanzadas

2. Seguridad
   ├─ Validación de datos
   ├─ Permisos de usuario
   └─ Sanitización de entrada

3. Performance
   ├─ Índices en BD
   ├─ Caché
   └─ Optimización de queries
```

### De Aquí

```
Ahora que sabes CRUD, puedes:

✓ Hacer sitio de blog
✓ E-commerce
✓ Sistema de tareas
✓ Red social pequeña
✓ Aplicación de inventario

¡El límite es tu imaginación!
```

---

## 📖 Aprende Más

### Documentación
- 📖 [Django Models](https://docs.djangoproject.com/stable/topics/db/models/)
- 📖 [Django QuerySet](https://docs.djangoproject.com/stable/topics/db/queries/)

### Herramientas
- 🛠️ [Django Admin](https://docs.djangoproject.com/stable/ref/contrib/admin/)
- 🛠️ [Django Forms](https://docs.djangoproject.com/stable/topics/forms/)

---

## 🎓 Checklist

- [ ] Entiendo qué es CRUD
- [ ] Ejecuté el proyecto
- [ ] Creé un producto
- [ ] Vi lista de productos
- [ ] Edité un producto
- [ ] Eliminé un producto
- [ ] Modifiqué modelo
- [ ] Creé producto por admin

**Si marcaste todo ✓ → ¡Dominas CRUD! 🎉**

---

<div align="center">

## ✅ ¡Perfecto!

### Aprendiste CRUD

**Siguiente destino:**

[🎓 Lab LDOO](../lab\ doo/README.md) - Conceptos Avanzados

---

*Práctico | Claro | Profesional*

</div>
