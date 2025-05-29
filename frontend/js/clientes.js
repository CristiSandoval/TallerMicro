// Elementos del DOM
const clientesTableBody = document.getElementById('clientes-table-body');
const clienteModal = document.getElementById('cliente-modal');
const clienteForm = document.getElementById('cliente-form');
const modalTitle = document.getElementById('modal-title');

// Variables de estado
let editandoCliente = null;

// Cargar clientes al iniciar la página
document.addEventListener('DOMContentLoaded', cargarClientes);

async function cargarClientes() {
    try {
        const clientes = await clienteService.obtenerTodos();
        mostrarClientes(clientes);
    } catch (error) {
        console.error('Error al cargar clientes:', error);
    }
}

function mostrarClientes(clientes) {
    clientesTableBody.innerHTML = '';
    clientes.forEach(cliente => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${cliente.id}</td>
            <td>${cliente.nombre}</td>
            <td>${cliente.email}</td>
            <td>${cliente.telefono}</td>
            <td>${cliente.numeroLicencia}</td>
            <td>
                <span class="estado-badge ${cliente.estado.toLowerCase()}">
                    ${cliente.estado}
                </span>
            </td>
            <td>
                <button class="btn btn-primary" onclick="editarCliente(${cliente.id})">
                    Editar
                </button>
                <button class="btn btn-danger" onclick="eliminarCliente(${cliente.id})">
                    Eliminar
                </button>
            </td>
        `;
        clientesTableBody.appendChild(row);
    });
}

function abrirModalCliente() {
    editandoCliente = null;
    modalTitle.textContent = 'Nuevo Cliente';
    clienteForm.reset();
    clienteModal.style.display = 'block';
}

function cerrarModalCliente() {
    clienteModal.style.display = 'none';
    clienteForm.reset();
    editandoCliente = null;
}

async function editarCliente(id) {
    try {
        const cliente = await clienteService.obtenerPorId(id);
        editandoCliente = cliente;
        modalTitle.textContent = 'Editar Cliente';
        
        // Llenar el formulario con los datos del cliente
        document.getElementById('cliente-id').value = cliente.id;
        document.getElementById('nombre').value = cliente.nombre;
        document.getElementById('email').value = cliente.email;
        document.getElementById('telefono').value = cliente.telefono;
        document.getElementById('numeroLicencia').value = cliente.numeroLicencia;
        document.getElementById('estado').value = cliente.estado;
        
        clienteModal.style.display = 'block';
    } catch (error) {
        console.error('Error al cargar el cliente:', error);
    }
}

async function guardarCliente(event) {
    event.preventDefault();
    
    const clienteData = {
        nombre: document.getElementById('nombre').value,
        email: document.getElementById('email').value,
        telefono: document.getElementById('telefono').value,
        numeroLicencia: document.getElementById('numeroLicencia').value,
        estado: document.getElementById('estado').value
    };

    try {
        if (editandoCliente) {
            await clienteService.actualizar(editandoCliente.id, clienteData);
        } else {
            await clienteService.crear(clienteData);
        }
        
        cerrarModalCliente();
        cargarClientes();
    } catch (error) {
        console.error('Error al guardar el cliente:', error);
    }
}

async function eliminarCliente(id) {
    if (!confirm('¿Está seguro de que desea eliminar este cliente?')) {
        return;
    }

    try {
        await clienteService.eliminar(id);
        cargarClientes();
    } catch (error) {
        console.error('Error al eliminar el cliente:', error);
    }
}

// Cerrar modal al hacer clic fuera de él
window.onclick = function(event) {
    if (event.target === clienteModal) {
        cerrarModalCliente();
    }
}; 