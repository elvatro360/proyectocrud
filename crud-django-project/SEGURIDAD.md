# 🔐 Guía de Seguridad - Proyecto CRUD Django

## ⚠️ Estado Actual: NO APTO PARA PRODUCCIÓN

Análisis de vulnerabilidades y recomendaciones específicas para este proyecto.

---

## 📋 Índice de Vulnerabilidades

1. [Críticas](#-críticas)
2. [Altas](#-altas)
3. [Medias](#-medias)
4. [Bajas](#-bajas)
5. [Checklist](#checklist-de-implementación)

---

## 🔴 CRÍTICAS

### 1. SECRET_KEY Expuesta en Código

**Ubicación:** `mysite/settings.py` línea 23

```python
# ❌ INSEGURO
SECRET_KEY = 'django-insecure--dc8_(g!w3ry7s64ek_yn0mc7@!xz@%beh2=^#atds%w3jtrgq'
```

**Riesgo:** Django usa esta clave para firmar sesiones y CSRF

**Solución:**

```python
# ✅ SEGURO
import os
SECRET_KEY = os.environ.get('SECRET_KEY', 'dev-key-only')
```

**En `.env`:**
```
SECRET_KEY=nueva-clave-segura-64-caracteres-aqui
```

---

### 2. DEBUG=True en Producción

```python
# ❌ INSEGURO
DEBUG = True
```

**Solución:**

```python
# ✅ SEGURO
DEBUG = os.environ.get('DEBUG', 'False') == 'True'
```

---

### 3. Sin Autenticación en Operaciones CRUD

**Problema:** Cualquiera puede crear, editar, borrar productos

**Solución - Opción A: Decoradores de login**

```python
from django.contrib.auth.decorators import login_required

@login_required
def crear_producto(request):
    # ... código ...

@login_required
def editar_producto(request, pk):
    # ... código ...

@login_required
def borrar_producto(request, pk):
    # ... código ...
```

**Solución - Opción B: Class-based Views**

```python
from django.views.generic import ListView, CreateView, UpdateView, DeleteView
from django.contrib.auth.mixins import LoginRequiredMixin

class CrearProductoView(LoginRequiredMixin, CreateView):
    model = Producto
    form_class = ProductoForm
    template_name = 'cruDapp/form_producto.html'
    success_url = reverse_lazy('lista_productos')

class EditarProductoView(LoginRequiredMixin, UpdateView):
    model = Producto
    form_class = ProductoForm
    template_name = 'cruDapp/form_producto.html'

class BorrarProductoView(LoginRequiredMixin, DeleteView):
    model = Producto
    success_url = reverse_lazy('lista_productos')
```

---

### 4. Sin HTTPS / TLS

**Solución (igual a landingPage):**

```python
SECURE_SSL_REDIRECT = True
SESSION_COOKIE_SECURE = True
CSRF_COOKIE_SECURE = True
SECURE_HSTS_SECONDS = 31536000
```

---

## 🔴 ALTAS

### 5. SQLite para Producción

**Problema:** No multiusuario, sin backup, sin escalabilidad

**Solución - PostgreSQL:**

```python
DATABASES = {
    'default': {
        'ENGINE': 'django.db.backends.postgresql',
        'NAME': os.environ.get('DB_NAME'),
        'USER': os.environ.get('DB_USER'),
        'PASSWORD': os.environ.get('DB_PASSWORD'),
        'HOST': os.environ.get('DB_HOST'),
        'PORT': os.environ.get('DB_PORT', '5432'),
    }
}
```

---

### 6. Sin Validación de Descripción

**Problema:** Campo sin límite de tamaño (DoS)

**Solución:**

```python
# forms.py
class ProductoForm(forms.ModelForm):
    class Meta:
        model = Producto
        fields = ['nombre', 'descripcion', 'precio']
    
    def clean_descripcion(self):
        desc = self.cleaned_data['descripcion']
        if len(desc) > 5000:  # Límite: 5000 caracteres
            raise forms.ValidationError("Descripción muy larga (máx 5000 caracteres)")
        
        # Eliminar espacios en blanco excesivos
        return desc.strip()
    
    def clean_precio(self):
        precio = self.cleaned_data['precio']
        if precio < 0:
            raise forms.ValidationError("El precio no puede ser negativo")
        if precio > 999999.99:
            raise forms.ValidationError("Precio máximo: $999,999.99")
        return precio
```

**En modelo:**

```python
# models.py
class Producto(models.Model):
    nombre = CharField(max_length=100)
    descripcion = TextField(max_length=5000)  # ← Añade límite
    precio = DecimalField(max_digits=10, decimal_places=2)
    created_at = DateTimeField(auto_now_add=True)  # ← Añade auditoría
    updated_at = DateTimeField(auto_now=True)      # ← Añade auditoría
```

---

### 7. Sin Rate Limiting

**Solución:**

```bash
pip install django-ratelimit
```

```python
# views.py
from django_ratelimit.decorators import ratelimit

@ratelimit(key='ip', rate='30/h', method='POST')
def crear_producto(request):
    # ... código ...
```

---

### 8. Sin Logs Estructurados

**Solución:**

```python
# settings.py
LOGGING = {
    'version': 1,
    'disable_existing_loggers': False,
    'handlers': {
        'file': {
            'level': 'INFO',
            'class': 'logging.handlers.RotatingFileHandler',
            'filename': 'logs/crud.log',
            'maxBytes': 1024 * 1024 * 10,
            'backupCount': 5,
        },
    },
    'loggers': {
        'cruDapp': {
            'handlers': ['file'],
            'level': 'INFO',
            'propagate': True,
        },
    },
}
```

**Usar en vistas:**

```python
import logging
logger = logging.getLogger('cruDapp')

def crear_producto(request):
    if request.method == 'POST':
        logger.info(f"Usuario {request.user} intenta crear producto")
        form = ProductoForm(request.POST)
        if form.is_valid():
            logger.info(f"Producto creado: {form.cleaned_data['nombre']}")
        else:
            logger.warning(f"Error en creación: {form.errors}")
```

---

## 🟡 MEDIAS

### 9. Sin Auditoría de Cambios

**Problema:** No sé quién cambió qué y cuándo

**Solución:**

```python
# models.py
from django.contrib.auth.models import User

class Producto(models.Model):
    nombre = CharField(max_length=100)
    descripcion = TextField(max_length=5000)
    precio = DecimalField(max_digits=10, decimal_places=2)
    
    # Auditoría
    created_by = ForeignKey(User, on_delete=SET_NULL, null=True, related_name='productos_creados')
    created_at = DateTimeField(auto_now_add=True)
    updated_by = ForeignKey(User, on_delete=SET_NULL, null=True, related_name='productos_editados')
    updated_at = DateTimeField(auto_now=True)
```

**En vistas:**

```python
def crear_producto(request):
    if request.method == 'POST':
        form = ProductoForm(request.POST)
        if form.is_valid():
            producto = form.save(commit=False)
            producto.created_by = request.user  # ← Quién creó
            producto.updated_by = request.user
            producto.save()
            return redirect('lista_productos')
```

---

### 10. Sin XSS Protection Adicional

**Solución - Bleach:**

```bash
pip install bleach
```

```python
# forms.py
import bleach

class ProductoForm(forms.ModelForm):
    def clean_descripcion(self):
        desc = self.cleaned_data['descripcion']
        # Permitir solo texto, sin HTML
        desc = bleach.clean(desc, tags=[], strip=True)
        return desc
```

---

### 11. Sin Permisos Granulares

**Solución:**

```python
# views.py
from django.contrib.auth.decorators import permission_required

@permission_required('cruDapp.add_producto')
def crear_producto(request):
    # ...

@permission_required('cruDapp.change_producto')
def editar_producto(request, pk):
    # ...

@permission_required('cruDapp.delete_producto')
def borrar_producto(request, pk):
    # ...
```

---

## 🟢 BAJAS

### 12. Sin Content Security Policy

```python
# settings.py
CSP_DEFAULT_SRC = ("'self'",)
CSP_SCRIPT_SRC = ("'self'",)
CSP_STYLE_SRC = ("'self'", "'unsafe-inline'")
```

---

### 13. Sin X-Frame-Options

```python
X_FRAME_OPTIONS = 'DENY'  # Previene clickjacking
```

---

## ✅ Checklist de Implementación

### Críticos (Haz hoy - 1 hora)
```
[ ] SECRET_KEY en .env
[ ] DEBUG = False
[ ] Autenticación en operaciones CRUD
[ ] HTTPS/TLS configurado
[ ] ALLOWED_HOSTS correcto
```

### Altos (Próximos días - 3-4 horas)
```
[ ] Migrar a PostgreSQL
[ ] Rate limiting
[ ] Validación de descripción
[ ] Logs estructurados
[ ] python manage.py check --deploy (pasa)
```

### Medios (Próxima semana - 2-3 horas)
```
[ ] Auditoría de cambios
[ ] Bleach para XSS
[ ] Permisos granulares
[ ] Monitoreo (Sentry)
```

---

## 🔧 Configuración Rápida

### `.env` (Crear - NO commitear)

```env
# Seguridad
SECRET_KEY=tu-clave-nueva-64-caracteres
DEBUG=False
ALLOWED_HOSTS=localhost,127.0.0.1,tudominio.com

# Base datos
DATABASE_URL=postgresql://user:pass@localhost/crud_db

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

DATABASES = {
    'default': dj_database_url.config(
        default=config('DATABASE_URL', default='sqlite:///db.sqlite3'),
        conn_max_age=600
    )
}

SECURE_SSL_REDIRECT = config('SECURE_SSL_REDIRECT', default=False, cast=bool)
```

---

## 📊 Tabla de Impacto

| # | Problema | Riesgo | Esfuerzo | Prioridad |
|---|----------|--------|----------|-----------|
| 1 | SECRET_KEY expuesta | Alto | 5 min | 🔴 |
| 2 | DEBUG=True | Alto | 1 min | 🔴 |
| 3 | Sin autenticación | Crítico | 30 min | 🔴 |
| 4 | Sin HTTPS | Alto | 30 min | 🔴 |
| 5 | SQLite producción | Alto | 2h | 🔴 |
| 6 | Sin validación | Medio | 30 min | 🟡 |
| 7 | Sin rate limiting | Medio | 20 min | 🟡 |
| 8 | Sin logs | Medio | 30 min | 🟡 |
| 9 | Sin auditoría | Bajo | 1h | 🟢 |
| 10 | Sin XSS protection | Bajo | 20 min | 🟢 |
| 11 | Sin permisos | Bajo | 30 min | 🟢 |
| 12 | Sin CSP | Bajo | 20 min | 🟢 |
| 13 | Sin X-Frame | Bajo | 5 min | 🟢 |

---

## ⏱️ Tiempo Total de Remediación

```
Críticos:  ~1-2 horas
Altos:     ~4-6 horas
Medios:    ~2-3 horas
Bajos:     ~1 hora
───────────────────
TOTAL:     ~8-12 horas (1-2 días)
```

---

## 🎯 Orden Recomendado

**Día 1 (Mañana):**
1. Mover SECRET_KEY a .env
2. DEBUG = False
3. ALLOWED_HOSTS correcto
4. Agregar autenticación

**Día 1 (Tarde):**
5. HTTPS configurado
6. Rate limiting
7. Validación de entrada

**Día 2:**
8. PostgreSQL
9. Logging
10. Auditoría
11. Monitoreo

---

## 🔍 Verificación

```bash
# Validar configuración
python manage.py check --deploy

# Ver qué cambió
python manage.py makemigrations

# Aplicar cambios
python manage.py migrate

# Ver logs
tail -f logs/crud.log
```

---

## 📚 Referencias

- [Django Security](https://docs.djangoproject.com/en/5.0/topics/security/)
- [Deployment Checklist](https://docs.djangoproject.com/en/5.0/howto/deployment/checklist/)
- [OWASP Top 10](https://owasp.org/www-project-top-ten/)

---

## 🎓 Conclusión

**Vulnerabilidades:** 13 encontradas  
**Criticidad:** 3 críticas + 4 altas  
**Esfuerzo:** 1-2 días de trabajo  

**Recomendación:** ⛔ NO desplegar sin hacer al menos los críticos.
