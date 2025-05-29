// Elementos del DOM
const vehiculosTableBody = document.getElementById('vehiculos-table-body');
const vehiculoModal = document.getElementById('vehiculo-modal');
const vehiculoForm = document.getElementById('vehiculo-form');
const modalTitle = document.getElementById('modal-title');

// Variables de estado
let editandoVehiculo = null;

// Cargar vehículos al iniciar la página
document.addEventListener('DOMContentLoaded', cargarVehiculos);

async function cargarVehiculos() {
    try {
        const vehiculos = await vehiculoService.obtenerTodos();
        mostrarVehiculos(vehiculos);
    } catch (error) {
        console.error('Error al cargar vehículos:', error);
    }
}

function mostrarVehiculos(vehiculos) {
    vehiculosTableBody.innerHTML = '';
    vehiculos.forEach(vehiculo => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${vehiculo.id}</td>
            <td>${vehiculo.marca}</td>
            <td>${vehiculo.modelo}</td>
            <td>${vehiculo.placa}</td>
            <td>${vehiculo.anio}</td>
            <td>$${vehiculo.tarifaDiaria}</td>
            <td>
                <span class="estado-badge ${vehiculo.estado.toLowerCase()}">
                    ${vehiculo.estado}
                </span>
            </td>
            <td>
                <button class="btn btn-primary" onclick="editarVehiculo(${vehiculo.id})">
                    Editar
                </button>
                <button class="btn btn-danger" onclick="eliminarVehiculo(${vehiculo.id})">
                    Eliminar
                </button>
            </td>
        `;
        vehiculosTableBody.appendChild(row);
    });
}

function abrirModalVehiculo() {
    editandoVehiculo = null;
    modalTitle.textContent = 'Nuevo Vehículo';
    vehiculoForm.reset();
    vehiculoModal.style.display = 'block';
}

function cerrarModalVehiculo() {
    vehiculoModal.style.display = 'none';
    vehiculoForm.reset();
    editandoVehiculo = null;
}

async function editarVehiculo(id) {
    try {
        const vehiculo = await vehiculoService.obtenerPorId(id);
        editandoVehiculo = vehiculo;
        modalTitle.textContent = 'Editar Vehículo';
        
        // Llenar el formulario con los datos del vehículo
        document.getElementById('vehiculo-id').value = vehiculo.id;
        document.getElementById('marca').value = vehiculo.marca;
        document.getElementById('modelo').value = vehiculo.modelo;
        document.getElementById('placa').value = vehiculo.placa;
        document.getElementById('anio').value = vehiculo.anio;
        document.getElementById('tarifaDiaria').value = vehiculo.tarifaDiaria;
        document.getElementById('estado').value = vehiculo.estado;
        
        vehiculoModal.style.display = 'block';
    } catch (error) {
        console.error('Error al cargar el vehículo:', error);
    }
}

async function guardarVehiculo(event) {
    event.preventDefault();
    
    const vehiculoData = {
        marca: document.getElementById('marca').value,
        modelo: document.getElementById('modelo').value,
        placa: document.getElementById('placa').value,
        anio: parseInt(document.getElementById('anio').value),
        tarifaDiaria: parseFloat(document.getElementById('tarifaDiaria').value),
        estado: document.getElementById('estado').value
    };

    try {
        if (editandoVehiculo) {
            await vehiculoService.actualizar(editandoVehiculo.id, vehiculoData);
        } else {
            await vehiculoService.crear(vehiculoData);
        }
        
        cerrarModalVehiculo();
        cargarVehiculos();
    } catch (error) {
        console.error('Error al guardar el vehículo:', error);
    }
}

async function eliminarVehiculo(id) {
    if (!confirm('¿Está seguro de que desea eliminar este vehículo?')) {
        return;
    }

    try {
        await vehiculoService.eliminar(id);
        cargarVehiculos();
    } catch (error) {
        console.error('Error al eliminar el vehículo:', error);
    }
}

// Cerrar modal al hacer clic fuera de él
window.onclick = function(event) {
    if (event.target === vehiculoModal) {
        cerrarModalVehiculo();
    }
}; 