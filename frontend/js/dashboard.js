// Elementos del DOM
const vehiculosDisponiblesElement = document.getElementById('vehiculos-disponibles');
const reservasActivasElement = document.getElementById('reservas-activas');
const totalClientesElement = document.getElementById('total-clientes');

// Función para cargar los datos del dashboard
async function cargarDatosDashboard() {
    try {
        // Obtener vehículos disponibles
        const vehiculos = await vehiculoService.obtenerDisponibles();
        vehiculosDisponiblesElement.textContent = vehiculos.length;

        // Obtener reservas activas
        const reservas = await reservaService.obtenerTodas();
        const reservasActivas = reservas.filter(reserva => 
            reserva.estado === 'CONFIRMADA' || reserva.estado === 'EN_PROGRESO'
        );
        reservasActivasElement.textContent = reservasActivas.length;

        // Obtener total de clientes
        const clientes = await clienteService.obtenerTodos();
        totalClientesElement.textContent = clientes.length;

    } catch (error) {
        console.error('Error al cargar datos del dashboard:', error);
    }
}

// Cargar datos al iniciar la página
document.addEventListener('DOMContentLoaded', cargarDatosDashboard);

// Actualizar datos cada 5 minutos
setInterval(cargarDatosDashboard, 5 * 60 * 1000); 