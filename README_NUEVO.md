# 🛒 CRUD de Productos - Aplicación Django

> **Estado:** 🟡 Desarrollo | **Tipo:** Gestor de productos  
> **Versión:** 1.0 | **Python:** 3.8+ | **Django:** 5.1.2

---

## 📋 Índice

- [¿Qué es?](#-qué-es)
- [Quick Start](#-quick-start)
- [Funcionalidades](#-funcionalidades)
- [Documentación](#-documentación)
- [Estructura](#-estructura)
- [Seguridad](#-seguridad--crítico)
- [Despliegue](#-despliegue)

---

## 🎯 ¿Qué es?

Aplicación CRUD (Create, Read, Update, Delete) desarrollada con Django 5.1.2 para gestionar productos.

**Características:**
- ✅ Lista de productos con paginación
- ✅ Crear nuevo producto
- ✅ Ver detalles de producto
- ✅ Editar producto existente
- ✅ Eliminar producto con confirmación
- ✅ Validación de formularios
- ✅ Admin de Django integrado

**Uso:** Gestión de inventario, tienda online, almacén

---

## 🚀 Quick Start

### 1. Requisitos
```bash
python --version  # Mínimo 3.8
pip --version
```

### 2. Configuración
```bash
# Clonar/abrir carpeta
cd proyectocrud

# Entorno virtual
python -m venv venv
.\venv\Scripts\activate        # Windows
source venv/bin/activate       # Linux/Mac

# Instalar dependencias
pip install -r requiriments.txt

# Migraciones
python manage.py migrate

# Crear admin
python manage.py createsuperuser
```

### 3. Ejecutar
```bash
python manage.py runserver

# Acceder a:
# http://localhost:8000/                  (Lista)
# http://localhost:8000/producto/nuevo/   (Crear)
# http://localhost:8000/admin/            (Admin)
```

---

## ✨ Funcionalidades

### CRUD Completo

| Operación | URL | Método | Descripción |
|-----------|-----|--------|-------------|
| **CREATE** | `/producto/nuevo/` | GET/POST | Crear nuevo producto |
| **READ** | `/` | GET | Ver lista de productos |
| **READ** | `/producto/<id>/` | GET | Ver detalle de producto |
| **UPDATE** | `/producto/<id>/editar/` | GET/POST | Editar producto |
| **DELETE** | `/producto/<id>/borrar/` | GET/POST | Eliminar producto |

### Modelo: Producto

```python
class Producto:
    nombre: str           # Máximo 100 caracteres
    descripcion: str      # Texto sin límite (⚠️)
    precio: decimal       # Formato: 9999.99
```

---

## 📚 Documentación

### Documentos Técnicos

| Documento | Contenido | Para |
|-----------|----------|------|
| **[MEMORIA_TECNICA.md](MEMORIA_TECNICA.md)** | Arquitectura MVT, flujos | Desarrolladores |
| **[SEGURIDAD.md](SEGURIDAD.md)** | 13 vulnerabilidades + fixes | DevOps/Seguridad |
| **[INDICE_SEGURIDAD.md](INDICE_SEGURIDAD.md)** | Checklist ejecutivo | Gerentes |

### Guía de Lectura

**👨‍💻 Desarrollador:**
1. Quick Start (arriba)
2. MEMORIA_TECNICA.md
3. SEGURIDAD.md

**🔒 DevOps:**
1. INDICE_SEGURIDAD.md
2. SEGURIDAD.md
3. Sección Despliegue

**📊 Product:**
1. ¿Qué es? (arriba)
2. Funcionalidades

---

## 📁 Estructura

```
proyectocrud/
├── manage.py                       ← CLI Django
├── db.sqlite3                      ← BD (desarrollo)
├── requiriments.txt                ← Dependencias
│
├── mysite/                         ← Configuración
│   ├── settings.py                 ← ⚠️ Revisar seguridad
│   ├── urls.py                     ← Rutas principales
│   ├── wsgi.py
│   └── asgi.py
│
├── cruDapp/                        ← App CRUD
│   ├── views.py                    ← 5 vistas CRUD
│   ├── models.py                   ← Modelo Producto
│   ├── forms.py                    ← Formulario
│   ├── urls.py                     ← Rutas app
│   ├── admin.py                    ← Configuración admin
│   │
│   ├── templates/
│   │   ├── lista_productos.html
│   │   ├── detalle_producto.html
│   │   ├── form_producto.html
│   │   └── confirmar_borrar.html
│   │
│   └── migrations/
│       └── 0001_initial.py
│
├── MEMORIA_TECNICA.md              ← Documentación técnica
├── SEGURIDAD.md                    ← Análisis seguridad
└── INDICE_SEGURIDAD.md             ← Checklist rápido
```

---

## 🔐 SEGURIDAD ⚠️ CRÍTICO

### Estado: NO APTO PARA PRODUCCIÓN

**13 vulnerabilidades encontradas:**

🔴 **Críticas (4):**
1. SECRET_KEY expuesta en código
2. DEBUG=True (exposición de información)
3. Sin autenticación en operaciones CRUD
4. Sin HTTPS

🔴 **Altas (4):**
5. SQLite en producción
6. Sin validación de descripción
7. Sin rate limiting
8. Sin logs

🟡 **Medias (5):**
9. Sin auditoría de cambios
10. Sin XSS protection adicional
11. Sin permisos granulares
12. Sin CSP headers
13. Sin X-Frame-Options

### ✅ Próximos Pasos (Críticos)

**Inmediato (~1-2 horas):**
```
1. [ ] SECRET_KEY en .env
2. [ ] DEBUG = False
3. [ ] Agregar autenticación (@login_required)
4. [ ] HTTPS configurado
```

**Próximos días (~3-4 horas):**
```
5. [ ] Migrar a PostgreSQL
6. [ ] Validación de entrada
7. [ ] Rate limiting
8. [ ] Logging
```

**Ver completo:** [`INDICE_SEGURIDAD.md`](INDICE_SEGURIDAD.md)

---

## 🚀 Despliegue

### Desarrollo ✅
```bash
python manage.py runserver
# http://localhost:8000
```

### Staging 🟡
```
DEBUG = False
ALLOWED_HOSTS = ['staging.tudominio.com']
Base de datos: PostgreSQL
HTTPS: Certificado autofirmado (testing)
```

### Producción ❌ (No recomendado sin cambios)

**Requisitos:**
- ✅ Todos los cambios críticos
- ✅ HTTPS/TLS
- ✅ PostgreSQL
- ✅ Gunicorn + NGINX
- ✅ Autenticación
- ✅ Monitoreo

**Arquitectura recomendada:**
```
Cliente (HTTPS)
    ↓
NGINX (Proxy)
    ↓
Gunicorn (App)
    ↓
PostgreSQL (BD)
```

---

## 🔧 Configuración Rápida

### Crear `.env` (NO COMMITEAR)

```env
# Seguridad
SECRET_KEY=tu-clave-nueva-64-caracteres
DEBUG=False
ALLOWED_HOSTS=localhost,127.0.0.1,tudominio.com

# Base de datos
DATABASE_URL=postgresql://user:pass@localhost/crud

# HTTPS
SECURE_SSL_REDIRECT=True
SESSION_COOKIE_SECURE=True
CSRF_COOKIE_SECURE=True
```

### Cargar en `settings.py`

```python
from decouple import config

SECRET_KEY = config('SECRET_KEY')
DEBUG = config('DEBUG', default=False, cast=bool)
ALLOWED_HOSTS = config('ALLOWED_HOSTS', default='localhost').split(',')
```

---

## 👥 Contribuciones

### Reportar Issues
1. Describe el problema
2. Incluye pasos para reproducir
3. Versión de Python/Django

### Contribuir
1. Fork
2. Branch: `git checkout -b feature/mi-feature`
3. Commit: `git commit -am "Descripción"`
4. Push: `git push origin feature/mi-feature`
5. Pull Request

---

## 📊 Estadísticas

| Métrica | Valor |
|---------|-------|
| **Líneas código** | ~100 |
| **Modelos** | 1 |
| **Vistas** | 5 |
| **URLs** | 5 |
| **Plantillas** | 5 |
| **Dependencias** | 1 (Django) |
| **Complejidad** | Baja |
| **Setup time** | 5 min |

---

## 🎓 Conceptos Clave

### CRUD Operations

```
CREATE  → Crear nuevo recurso    (POST)
READ    → Leer recurso           (GET)
UPDATE  → Actualizar recurso     (POST/PUT)
DELETE  → Eliminar recurso       (DELETE)
```

### Django MVT

```
Model    → Base de datos (models.py)
View     → Lógica de negocio (views.py)
Template → Presentación (HTML)
```

### Flujos de Datos

```
GET /
└─→ lista_productos()
    └─→ Producto.objects.all()
        └─→ lista_productos.html

POST /producto/nuevo/
└─→ crear_producto()
    └─→ Valida formulario
        └─→ Guarda en BD
            └─→ Redirect
```

---

## ⚖️ Licencia

[Especificar tu licencia - MIT, Apache, etc.]

---

## 👨‍💼 Autor

**Fernando Pérez**  
GitHub: [@tuusuario]  
Email: tu@email.com

---

## 🎯 Resumen

```
✅ Funciona perfectamente en desarrollo
⚠️ 13 vulnerabilidades encontradas
🔴 4 críticas que arreglar
⏱️ 1-2 horas para lo crítico
📅 1-2 días para producción
```

**Próximo paso:** Leer [`INDICE_SEGURIDAD.md`](INDICE_SEGURIDAD.md)

---

<div align="center">

**[📖 MEMORIA_TECNICA.md](MEMORIA_TECNICA.md)** · **[🔐 SEGURIDAD.md](SEGURIDAD.md)** · **[⚡ INDICE_SEGURIDAD.md](INDICE_SEGURIDAD.md)**

🟡 Desarrollo | ❌ No producción (aún)

</div>
