// Configuración de la API
const API_CONFIG = {
    baseUrl: 'http://localhost:8080',
    endpoints: {
        vehiculos: {
            base: 'http://localhost:8081/api/vehiculos',
            disponibles: 'http://localhost:8081/api/vehiculos?estado=DISPONIBLE'
        },
        clientes: {
            base: 'http://localhost:8082/api/clientes'
        },
        reservas: {
            base: 'http://localhost:8083/api/reservas'
        }
    }
};

// Funciones de utilidad para manejar errores
const handleError = (error) => {
    console.error('Error:', error);
    alert('Ha ocurrido un error. Por favor, intente nuevamente.');
};

// Funciones genéricas para llamadas a la API
const api = {
    async get(url) {
        try {
            const response = await fetch(url);
            if (!response.ok) throw new Error('Error en la petición');
            return await response.json();
        } catch (error) {
            handleError(error);
            throw error;
        }
    },

    async post(url, data) {
        try {
            const response = await fetch(url, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(data)
            });
            if (!response.ok) throw new Error('Error en la petición');
            return await response.json();
        } catch (error) {
            handleError(error);
            throw error;
        }
    },

    async put(url, data) {
        try {
            const response = await fetch(url, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(data)
            });
            if (!response.ok) throw new Error('Error en la petición');
            return await response.json();
        } catch (error) {
            handleError(error);
            throw error;
        }
    },

    async delete(url) {
        try {
            const response = await fetch(url, {
                method: 'DELETE'
            });
            if (!response.ok) throw new Error('Error en la petición');
            return true;
        } catch (error) {
            handleError(error);
            throw error;
        }
    },

    async patch(url, data) {
        try {
            const response = await fetch(url, {
                method: 'PATCH',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(data)
            });
            if (!response.ok) throw new Error('Error en la petición');
            return await response.json();
        } catch (error) {
            handleError(error);
            throw error;
        }
    }
};

// Funciones específicas para cada entidad
const vehiculoService = {
    async obtenerTodos() {
        return api.get(API_CONFIG.endpoints.vehiculos.base);
    },

    async obtenerDisponibles() {
        return api.get(API_CONFIG.endpoints.vehiculos.disponibles);
    },

    async obtenerPorId(id) {
        return api.get(`${API_CONFIG.endpoints.vehiculos.base}/${id}`);
    },

    async crear(vehiculo) {
        return api.post(API_CONFIG.endpoints.vehiculos.base, vehiculo);
    },

    async actualizar(id, vehiculo) {
        return api.put(`${API_CONFIG.endpoints.vehiculos.base}/${id}`, vehiculo);
    },

    async actualizarEstado(id, estado) {
        return api.patch(`${API_CONFIG.endpoints.vehiculos.base}/${id}/estado?estado=${estado}`);
    },

    async eliminar(id) {
        return api.delete(`${API_CONFIG.endpoints.vehiculos.base}/${id}`);
    }
};

const clienteService = {
    async obtenerTodos() {
        return api.get(API_CONFIG.endpoints.clientes.base);
    },

    async obtenerPorId(id) {
        return api.get(`${API_CONFIG.endpoints.clientes.base}/${id}`);
    },

    async crear(cliente) {
        return api.post(API_CONFIG.endpoints.clientes.base, cliente);
    },

    async actualizar(id, cliente) {
        return api.put(`${API_CONFIG.endpoints.clientes.base}/${id}`, cliente);
    },

    async eliminar(id) {
        return api.delete(`${API_CONFIG.endpoints.clientes.base}/${id}`);
    }
};

const reservaService = {
    async obtenerTodas() {
        return api.get(API_CONFIG.endpoints.reservas.base);
    },

    async obtenerPorId(id) {
        return api.get(`${API_CONFIG.endpoints.reservas.base}/${id}`);
    },

    async crear(reserva) {
        return api.post(API_CONFIG.endpoints.reservas.base, reserva);
    },

    async actualizarEstado(id, estado) {
        return api.patch(`${API_CONFIG.endpoints.reservas.base}/${id}/estado?estado=${estado}`);
    }
}; 