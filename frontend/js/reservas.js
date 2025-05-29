// Elementos del DOM
const reservasTableBody = document.getElementById('reservas-table-body');
const reservaModal = document.getElementById('reserva-modal');
const reservaForm = document.getElementById('reserva-form');
const modalTitle = document.getElementById('modal-title');
const clienteSelect = document.getElementById('cliente');
const vehiculoSelect = document.getElementById('vehiculo');
const fechaInicioInput = document.getElementById('fechaInicio');
const fechaFinInput = document.getElementById('fechaFin');
const totalInput = document.getElementById('total');

// Variables de estado
let editandoReserva = null;
let vehiculosDisponibles = [];

// Cargar datos al iniciar la página
document.addEventListener('DOMContentLoaded', async () => {
    await Promise.all([
        cargarReservas(),
        cargarClientes(),
        cargarVehiculos()
    ]);
});

// Funciones para cargar datos
async function cargarReservas() {
    try {
        const reservas = await reservaService.obtenerTodas();
        mostrarReservas(reservas);
    } catch (error) {
        console.error('Error al cargar reservas:', error);
    }
}

async function cargarClientes() {
    try {
        const clientes = await clienteService.obtenerTodos();
        clienteSelect.innerHTML = '<option value="">Seleccione un cliente</option>';
        clientes.forEach(cliente => {
            if (cliente.estado === 'ACTIVO') {
                clienteSelect.innerHTML += `
                    <option value="${cliente.id}">${cliente.nombre} - ${cliente.numeroLicencia}</option>
                `;
            }
        });
    } catch (error) {
        console.error('Error al cargar clientes:', error);
    }
}

async function cargarVehiculos() {
    try {
        vehiculosDisponibles = await vehiculoService.obtenerDisponibles();
        actualizarSelectVehiculos();
    } catch (error) {
        console.error('Error al cargar vehículos:', error);
    }
}

function actualizarSelectVehiculos() {
    vehiculoSelect.innerHTML = '<option value="">Seleccione un vehículo</option>';
    vehiculosDisponibles.forEach(vehiculo => {
        vehiculoSelect.innerHTML += `
            <option value="${vehiculo.id}" data-tarifa="${vehiculo.tarifaDiaria}">
                ${vehiculo.marca} ${vehiculo.modelo} - ${vehiculo.placa}
            </option>
        `;
    });
}

// Funciones para mostrar datos
function mostrarReservas(reservas) {
    reservasTableBody.innerHTML = '';
    reservas.forEach(reserva => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${reserva.id}</td>
            <td>${reserva.cliente.nombre}</td>
            <td>${reserva.vehiculo.marca} ${reserva.vehiculo.modelo}</td>
            <td>${formatearFecha(reserva.fechaInicio)}</td>
            <td>${formatearFecha(reserva.fechaFin)}</td>
            <td>$${reserva.total}</td>
            <td>
                <span class="estado-badge ${reserva.estado.toLowerCase()}">
                    ${reserva.estado}
                </span>
            </td>
            <td>
                <button class="btn btn-primary" onclick="editarReserva(${reserva.id})">
                    Editar
                </button>
                <button class="btn btn-danger" onclick="cancelarReserva(${reserva.id})">
                    Cancelar
                </button>
            </td>
        `;
        reservasTableBody.appendChild(row);
    });
}

// Funciones para el modal
function abrirModalReserva() {
    editandoReserva = null;
    modalTitle.textContent = 'Nueva Reserva';
    reservaForm.reset();
    reservaModal.style.display = 'block';
    establecerFechaMinima();
}

function cerrarModalReserva() {
    reservaModal.style.display = 'none';
    reservaForm.reset();
    editandoReserva = null;
}

async function editarReserva(id) {
    try {
        const reserva = await reservaService.obtenerPorId(id);
        editandoReserva = reserva;
        modalTitle.textContent = 'Editar Reserva';
        
        // Llenar el formulario con los datos de la reserva
        document.getElementById('reserva-id').value = reserva.id;
        document.getElementById('cliente').value = reserva.cliente.id;
        document.getElementById('vehiculo').value = reserva.vehiculo.id;
        document.getElementById('fechaInicio').value = formatearFechaInput(reserva.fechaInicio);
        document.getElementById('fechaFin').value = formatearFechaInput(reserva.fechaFin);
        document.getElementById('total').value = reserva.total;
        document.getElementById('estado').value = reserva.estado;
        
        reservaModal.style.display = 'block';
    } catch (error) {
        console.error('Error al cargar la reserva:', error);
    }
}

// Funciones para el formulario
async function guardarReserva(event) {
    event.preventDefault();
    
    const reservaData = {
        clienteId: parseInt(document.getElementById('cliente').value),
        vehiculoId: parseInt(document.getElementById('vehiculo').value),
        fechaInicio: new Date(document.getElementById('fechaInicio').value).toISOString(),
        fechaFin: new Date(document.getElementById('fechaFin').value).toISOString(),
        estado: document.getElementById('estado').value
    };

    try {
        if (editandoReserva) {
            await reservaService.actualizarEstado(editandoReserva.id, reservaData.estado);
        } else {
            await reservaService.crear(reservaData);
        }
        
        cerrarModalReserva();
        cargarReservas();
    } catch (error) {
        console.error('Error al guardar la reserva:', error);
    }
}

async function cancelarReserva(id) {
    if (!confirm('¿Está seguro de que desea cancelar esta reserva?')) {
        return;
    }

    try {
        await reservaService.actualizarEstado(id, 'CANCELADA');
        cargarReservas();
    } catch (error) {
        console.error('Error al cancelar la reserva:', error);
    }
}

// Funciones de utilidad
function formatearFecha(fecha) {
    return new Date(fecha).toLocaleString('es-ES', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit'
    });
}

function formatearFechaInput(fecha) {
    return new Date(fecha).toISOString().slice(0, 16);
}

function establecerFechaMinima() {
    const ahora = new Date();
    const fechaMinima = ahora.toISOString().slice(0, 16);
    fechaInicioInput.min = fechaMinima;
    fechaFinInput.min = fechaMinima;
}

// Eventos para calcular el total
fechaInicioInput.addEventListener('change', calcularTotal);
fechaFinInput.addEventListener('change', calcularTotal);
vehiculoSelect.addEventListener('change', calcularTotal);

function calcularTotal() {
    const fechaInicio = new Date(fechaInicioInput.value);
    const fechaFin = new Date(fechaFinInput.value);
    const vehiculoOption = vehiculoSelect.options[vehiculoSelect.selectedIndex];
    
    if (fechaInicio && fechaFin && vehiculoOption && vehiculoOption.dataset.tarifa) {
        const dias = Math.ceil((fechaFin - fechaInicio) / (1000 * 60 * 60 * 24));
        const tarifa = parseFloat(vehiculoOption.dataset.tarifa);
        const total = dias * tarifa;
        totalInput.value = total.toFixed(2);
    } else {
        totalInput.value = '';
    }
}

// Cerrar modal al hacer clic fuera de él
window.onclick = function(event) {
    if (event.target === reservaModal) {
        cerrarModalReserva();
    }
}; 