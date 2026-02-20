# 📘 Memoria Técnica - Proyecto CRUD Django

## 🎯 Resumen Ejecutivo

**Proyecto:** CRUD (Create, Read, Update, Delete) de Productos  
**Tipo:** Aplicación de gestión de datos  
**Stack:** Django 5.1.2 + SQLite + Bootstrap/HTML  
**Estado:** Desarrollo (⚠️ No apto para producción sin cambios)  
**Características:** Gestión completa de productos

---

## 📋 Índice

1. [Arquitectura General](#arquitectura-general)
2. [Componentes Principales](#componentes-principales)
3. [Flujo de Datos](#flujo-de-datos)
4. [Operaciones CRUD](#operaciones-crud)
5. [Base de Datos](#base-de-datos)
6. [URLs y Rutas](#urls-y-rutas)
7. [Seguridad Actual](#seguridad-actual)

---

## 🏗️ Arquitectura General

### Patrón de Diseño
- **Patrón:** MVT (Model-View-Template)
- **Framework:** Django 5.1.2
- **Tipo:** Aplicación CRUD funcional
- **Propósito:** Gestionar inventario de productos

### Stack Técnico

```
┌─────────────────────────────────────────┐
│      Frontend (HTML/Bootstrap)          │
│  (Plantillas Django + Forms)            │
└──────────────────┬──────────────────────┘
                   ↓
┌─────────────────────────────────────────┐
│   Capa de Aplicación (Django CRUD)      │
│  Views → Forms → Validación → Redireccion
└──────────────────┬──────────────────────┘
                   ↓
┌─────────────────────────────────────────┐
│      Capa de Datos (SQLite)             │
│  Modelo Producto ORM → Base de datos    │
└─────────────────────────────────────────┘
```

---

## 🧩 Componentes Principales

### 1. Modelo: `Producto` (`cruDapp/models.py`)

```python
class Producto(models.Model):
    nombre = CharField(max_length=100)
    descripcion = TextField()
    precio = DecimalField(max_digits=10, decimal_places=2)
    
    def __str__(self):
        return self.nombre
```

**Campos:**
| Campo | Tipo | Restricciones |
|-------|------|---------------|
| `nombre` | CharField | Máx 100 caracteres |
| `descripcion` | TextField | Sin límite (⚠️) |
| `precio` | DecimalField | 10 dígitos totales, 2 decimales |
| `id` | AutoField | Primary key (auto) |
| `created_at` | DateTimeField | ❌ NO IMPLEMENTADO |
| `updated_at` | DateTimeField | ❌ NO IMPLEMENTADO |

**Riesgo:** No hay campos de auditoría (created_at, updated_at)

### 2. Formulario: `ProductoForm` (`cruDapp/forms.py`)

```python
class ProductoForm(forms.ModelForm):
    class Meta:
        model = Producto
        fields = ['nombre', 'descripcion', 'precio']
```

**Validación:**
- ✅ Campo nombre: máximo 100 caracteres
- ✅ Campo email: validación de formato (no hay email)
- ⚠️ Campo descripción: sin límite de tamaño
- ✅ Campo precio: validación automática DecimalField

**Limitaciones:**
- ❌ Sin sanitización de entrada
- ❌ Sin validación personalizada
- ✅ Validación básica por tipo de campo

### 3. Vistas: Operaciones CRUD (`cruDapp/views.py`)

**5 Vistas funcionales:**

```python
1. crear_producto(request)       # CREATE
2. lista_productos(request)      # READ (Lista)
3. detalle_producto(request, pk) # READ (Detalle)
4. editar_producto(request, pk)  # UPDATE
5. borrar_producto(request, pk)  # DELETE
```

**Patrones de cada vista:**

| Vista | Método | Lógica | Validación |
|-------|--------|--------|-----------|
| `crear_producto` | GET/POST | Renderiza formulario, guarda si válido | ✅ Form valid |
| `lista_productos` | GET | Lista todos los productos | Ninguna |
| `detalle_producto` | GET | Obtiene un producto por ID | 404 si no existe |
| `editar_producto` | GET/POST | Edita un producto existente | ✅ Form valid |
| `borrar_producto` | GET/POST | Confirma y elimina | 404 si no existe |

### 4. URLs (`cruDapp/urls.py`)

```python
path('', views.lista_productos, name='lista_productos')
path('producto/<int:pk>/', views.detalle_producto, name='detalle_producto')
path('producto/nuevo/', views.crear_producto, name='crear_producto')
path('producto/<int:pk>/editar/', views.editar_producto, name='editar_producto')
path('producto/<int:pk>/borrar/', views.borrar_producto, name='borrar_producto')
```

**Rutas disponibles:**
```
GET  /                          → Lista de productos
GET  /producto/<id>/            → Detalle de producto
GET  /producto/nuevo/           → Formulario crear
POST /producto/nuevo/           → Crear producto
GET  /producto/<id>/editar/     → Formulario editar
POST /producto/<id>/editar/     → Guardar edición
GET  /producto/<id>/borrar/     → Confirmar borrar
POST /producto/<id>/borrar/     → Ejecutar borrar
```

### 5. Plantillas (`cruDapp/templates/`)

```
├── cruDapp/
│   ├── base.html               (Base común)
│   ├── lista_productos.html    (Lista)
│   ├── detalle_producto.html   (Detalle)
│   ├── form_producto.html      (Crear/Editar)
│   └── confirmar_borrar.html   (Confirmación)
```

---

## 🔄 Flujo de Datos

### Flujo CREATE (Crear Producto)

```
1. GET /producto/nuevo/
   └─→ crear_producto(GET)
       ├─→ Crea formulario vacío
       └─→ Renderiza form_producto.html

2. Usuario llena y POST /producto/nuevo/
   └─→ crear_producto(POST)
       ├─→ Valida ProductoForm
       ├─→ Si válido: form.save() → BD
       │   └─→ Redirect a lista_productos
       └─→ Si error: Re-renderiza con errores
```

### Flujo READ (Leer Productos)

```
1. GET /
   └─→ lista_productos(GET)
       ├─→ Obtiene: Producto.objects.all()
       └─→ Renderiza lista_productos.html

2. GET /producto/<id>/
   └─→ detalle_producto(GET)
       ├─→ Obtiene: get_object_or_404(Producto, pk=id)
       ├─→ Si existe: Renderiza detalle_producto.html
       └─→ Si no existe: 404 Error
```

### Flujo UPDATE (Editar Producto)

```
1. GET /producto/<id>/editar/
   └─→ editar_producto(GET)
       ├─→ Obtiene producto
       └─→ Renderiza form con datos pre-cargados

2. POST /producto/<id>/editar/
   └─→ editar_producto(POST)
       ├─→ Valida formulario con instancia
       ├─→ Si válido: form.save() → BD
       │   └─→ Redirect a detalle
       └─→ Si error: Re-renderiza
```

### Flujo DELETE (Borrar Producto)

```
1. GET /producto/<id>/borrar/
   └─→ borrar_producto(GET)
       ├─→ Obtiene producto
       └─→ Renderiza confirmar_borrar.html

2. POST /producto/<id>/borrar/
   └─→ borrar_producto(POST)
       ├─→ Obtiene producto
       ├─→ Ejecuta: producto.delete()
       └─→ Redirect a lista_productos
```

---

## 💾 Base de Datos

### Esquema: Tabla `cruDapp_producto`

```sql
CREATE TABLE cruDapp_producto (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT NOT NULL,
    precio DECIMAL(10, 2) NOT NULL
);
```

### Características Actuales

- **Motor:** SQLite (archivo: `db.sqlite3`)
- **Tabla:** `cruDapp_producto`
- **Registros:** Variable (según uso)
- **Indexes:** Solo PK
- **Relaciones:** Ninguna

### ⚠️ Limitaciones para Producción

```
SQLite NO ES RECOMENDABLE porque:
❌ No multiusuario (bloqueos de archivo)
❌ Sin replicación/backup
❌ Limitado en escala
❌ Sin transacciones robustas
❌ Sin usuarios/permisos
```

---

## ⚙️ Configuración

### `mysite/settings.py` - Estado Actual

```python
SECRET_KEY = 'django-insecure-...'  # ⚠️ EXPUESTA
DEBUG = True                        # ⚠️ NO PARA PRODUCCIÓN
ALLOWED_HOSTS = ['192.168.1.73']   # ⚠️ IP local solo
DATABASES = {'sqlite3': ...}        # ⚠️ NO para producción
STATIC_URL = 'static/'              # ✅ Básico OK
```

**Estado de seguridad:**
- ✅ MIDDLEWARE de seguridad configurado
- ❌ DEBUG = True (exposición de información)
- ❌ SECRET_KEY en código (exposición)
- ⚠️ ALLOWED_HOSTS limitado (OK desarrollo)

---

## 🔐 URLs y Rutas

### Enrutamiento Principal

**`mysite/urls.py`:**
```python
path('admin/', admin.site.urls)
path('', include('cruDapp.urls'))
```

**`cruDapp/urls.py`:**
```python
urlpatterns = [
    path('', lista_productos),
    path('producto/<int:pk>/', detalle_producto),
    path('producto/nuevo/', crear_producto),
    path('producto/<int:pk>/editar/', editar_producto),
    path('producto/<int:pk>/borrar/', borrar_producto),
]
```

### Acceso a URLs

```
http://localhost:8000/                          → Lista
http://localhost:8000/producto/1/               → Detalle
http://localhost:8000/producto/nuevo/           → Crear
http://localhost:8000/producto/1/editar/        → Editar
http://localhost:8000/producto/1/borrar/        → Borrar
http://localhost:8000/admin/                    → Admin Django
```

---

## 📊 Diagrama de Flujo General

```
HTTP Request (/producto/nuevo/)
    │
    ▼
URL Router → Path matching
    │
    ▼
Middleware (CSRF, Auth, etc.)
    │
    ├─ GET  ─→ crear_producto (método GET)
    │         └─→ ProductoForm() (vacío)
    │         └─→ Renderiza form_producto.html
    │
    └─ POST ─→ crear_producto (método POST)
              ├─→ ProductoForm(request.POST)
              ├─→ Valida
              ├─→ Si válido: form.save() → BD
              │   └─→ Redirect lista_productos
              └─→ Si error: Re-renderiza form
```

---

## 🎓 Ciclo de Vida de Solicitud

```
1. Usuario accede /
2. Django router: urlpatterns → lista_productos
3. lista_productos(request):
   a. Ejecuta: Producto.objects.all()
   b. Obtiene: QuerySet con todos los productos
   c. Contexto: {'productos': productos}
   d. Renderiza: lista_productos.html
   e. Devuelve: HttpResponse (HTML)
4. Navegador renderiza la página
5. Usuario ve lista de productos
```

---

## 🔍 Análisis de Seguridad Actual

### Fortalezas ✅

- ✅ ORM de Django protege SQL injection
- ✅ Auto-escape de plantillas previene XSS básico
- ✅ CSRF protection en middleware
- ✅ Validación de tipos en formularios
- ✅ `get_object_or_404` previene acceso no autorizado

### Vulnerabilidades ⚠️

| # | Problema | Riesgo | Severidad |
|---|----------|--------|-----------|
| 1 | SECRET_KEY expuesta | Falsificación de sesiones | 🔴 CRÍTICO |
| 2 | DEBUG=True | Information disclosure | 🔴 CRÍTICO |
| 3 | SQLite producción | Sin backup/replicación | 🔴 ALTO |
| 4 | Sin HTTPS | Man-in-the-middle | 🔴 ALTO |
| 5 | Sin validación descripción | Spam en BD | 🟡 MEDIO |
| 6 | Sin limit de tamaño | DoS (BD grande) | 🟡 MEDIO |
| 7 | Sin autenticación | Acceso público | 🔴 ALTO |
| 8 | Sin rate limiting | Spam/DoS | 🔴 ALTO |
| 9 | Sin auditoría | No trazable | 🟡 MEDIO |
| 10 | Sin logs | No debuggeable | 🟡 MEDIO |

---

## 📈 Métricas del Proyecto

| Métrica | Valor |
|---------|-------|
| **Líneas de código** | ~100 |
| **Modelos** | 1 (Producto) |
| **Vistas** | 5 funciones |
| **URLs** | 5 rutas |
| **Plantillas** | 5 archivos |
| **Formularios** | 1 (ProductoForm) |
| **Dependencias** | 1 (Django) |
| **Complejidad** | Baja |
| **Escalabilidad** | Baja (SQLite) |

---

## 🚀 Despliegue Actual

### Desarrollo ✅
```bash
python manage.py runserver
# http://localhost:8000
```

### Producción ❌

No recomendado sin:
1. Variables de entorno
2. HTTPS/TLS
3. PostgreSQL
4. Gunicorn + NGINX
5. Autenticación
6. Rate limiting

---

## 🎯 Conclusión

**Proyecto CRUD:**
- ✅ Funcional para desarrollo
- ✅ Bien estructurado (MVT estándar)
- ✅ Código limpio
- ❌ **NO apto para producción**
- ⚠️ Requiere cambios de seguridad

**Ver:** [`SEGURIDAD.md`](SEGURIDAD.md) para recomendaciones de seguridad.
