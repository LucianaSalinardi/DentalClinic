window.addEventListener("load", function () {
  const baseUrl = "/clinical-dental/patients";

  let formCrearPaciente = document.querySelector(".form-crear");

  //CREAR PACIENTE

  formCrearPaciente.addEventListener("submit", function (event) {
    event.preventDefault();

    let textoCrear = document.querySelector(".form-crear .texto-exito");
    let textoCrearError = document.querySelector(".form-crear .texto-error");

    const inputNombrePaciente = document.getElementById("crear-nombre").value;
    const inputApellidoPaciente =
      document.getElementById("crear-apellido").value;
    const inputDni = document.getElementById("crear-dni").value;
    const inputCalle = document.getElementById("crear-calle").value;
    const inputNumero = document.getElementById("crear-numero").value;
    const inputLocalidad = document.getElementById("crear-localidad").value;
    const inputProvincia = document.getElementById("crear-provincia").value;
    const inputCodigoPostal = document.getElementById("crear-cp").value;
    const inputPais = document.getElementById("crear-pais").value;

    const direccion = {
      street: inputCalle,
      number: inputNumero,
      town: inputLocalidad,
      province: inputProvincia,
      postalCode: inputCodigoPostal,
      country: inputPais,
    };

    if (textoCrear.hasChildNodes) {
      textoCrear.innerHTML = "";
    }

    if (textoCrearError.hasChildNodes) {
      textoCrearError.innerHTML = "";
    }

    const datosPaciente = {
      firstName: inputNombrePaciente,
      lastName: inputApellidoPaciente,
      dni: inputDni,
      address: direccion,
    };

    const configuraciones = {
      method: "POST",
      headers: {
        "Content-type": "application/json",
      },
      body: JSON.stringify(datosPaciente),
    };

    fetch(baseUrl, configuraciones)
      .then((response) => {
        if (response.status == 400) {
          textoCrearError.innerHTML += `<p><small>Revisa los campos ingresados</small></p>`;
          return response.json();
        }

        if (response.status == 403) {
          Swal.fire({
            title: "No tienes permiso para realizar esta acción",
            icon: "alert",
            confirmButtonColor: "#008000",
            confirmButtonText: "OK",
          });
          return response.json();
        }

        if (response.status == 409) {
          textoCrearError.innerHTML += `<p><small>El paciente ya existe</small></p>`;
          return response.json();
        }

        if (response.ok) {
          textoCrear.innerHTML += `<p><small>Paciente creado con éxito</small></p>`;
          return response.json();
        }
      })
      .then((data) => {
        console.log(data);
      })
      .catch((error) => {
        console.log(error);
      });

    formCrearPaciente.reset();
  });

  //ACTUALIZAR PACIENTE

  let formActualizar = document.querySelector(".form-actualizar");
  let textoActualizar = document.querySelector(".form-actualizar .texto-exito");
  let textoActualizarError = document.querySelector(
    ".form-actualizar .texto-error"
  );

  formActualizar.addEventListener("submit", function (event) {
    event.preventDefault();

    const idActualizar = document.getElementById("actualizar-id").value;
    const inputActualizarNombre =
      document.getElementById("actualizar-nombre").value;
    const inputActualizarApellido = document.getElementById(
      "actualizar-apellido"
    ).value;

    const inputActualizarDni = document.getElementById("actualizar-dni").value;
    const inputIdAddress = document.getElementById("id-address").value;
    const inputActualizarCalle =
      document.getElementById("actualizar-calle").value;
    const inputActualizarNumero =
      document.getElementById("actualizar-numero").value;
    const inputActualizarLocalidad = document.getElementById(
      "actualizar-localidad"
    ).value;
    const inputActualizarProvincia = document.getElementById(
      "actualizar-provincia"
    ).value;
    const inputActualizarCodigoPostal =
      document.getElementById("actualizar-cp").value;
    const inputActualizarPais =
      document.getElementById("actualizar-pais").value;

    const direccionActualizada = {
      idAddress: inputIdAddress,
      street: inputActualizarCalle,
      number: inputActualizarNumero,
      town: inputActualizarLocalidad,
      province: inputActualizarProvincia,
      postalCode: inputActualizarCodigoPostal,
      country: inputActualizarPais,
    };

    if (textoActualizar.hasChildNodes) {
      textoActualizar.innerHTML = "";
    }

    if (textoActualizarError.hasChildNodes) {
      textoActualizarError.innerHTML = "";
    }

    const datosPaciente = {
      idPatient: idActualizar,
      firstName: inputActualizarNombre,
      lastName: inputActualizarApellido,
      dni: inputActualizarDni,
      address: direccionActualizada,
    };

    const configuraciones = {
      method: "PUT",
      headers: {
        "Content-type": "application/json",
      },
      body: JSON.stringify(datosPaciente),
    };

    fetch(baseUrl, configuraciones)
      .then((response) => {
        if (response.status == 400) {
          textoActualizarError.innerHTML += `<p><small>Revisa los campos ingresados</small></p>`;
          return response.json();
        }

        if (response.status == 403) {
          Swal.fire({
            title: "No tienes permiso para realizar esta acción",
            icon: "alert",
            confirmButtonColor: "#008000",
            confirmButtonText: "OK",
          });
          return response.json();
        }

        if (response.status == 404) {
          textoActualizarError.innerHTML += `<p><small>El paciente asociado con ese domicilio no existe</small></p>`;
          return response.json();
        }

        if (response.ok) {
          textoActualizar.innerHTML += `<p><small>Paciente actualizado con éxito</small></p>`;
          return response.json();
        }
      })
      .then((data) => {
        console.log(data);
      })
      .catch((error) => {
        console.log(error);
      });

    formActualizar.reset();
  });

  //LISTAR TODOS LOS PACIENTES

  let botonListarTodos = document.querySelector(".listar-p button");

  botonListarTodos.addEventListener("click", function (event) {
    event.preventDefault();

    const configuraciones = {
      method: "GET",
      headers: {},
    };

    fetch(baseUrl, configuraciones)
      .then((response) => {
        if (response.status == 403) {
          Swal.fire({
            title: "No tienes permiso para realizar esta acción",
            icon: "alert",
            confirmButtonColor: "#008000",
            confirmButtonText: "OK",
          });
          return response.json();
        }

        return response.json();
      })
      .then((data) => {
        renderizarPacientes(data);
      })
      .catch((error) => console.log(error));
  });

  // BUSCAR POR ID

  let botonBuscarPorId = document.querySelector(".buscar-paciente");
  let textoBuscarError = document.querySelector(
    ".buscar-paciente .texto-error"
  );

  botonBuscarPorId.addEventListener("submit", function (event) {
    event.preventDefault();

    const id = document.getElementById("id-paciente").value;

    if (textoBuscarError.hasChildNodes) {
      textoBuscarError.innerHTML = "";
    }

    const configuraciones = {
      method: "GET",
      headers: {},
    };

    fetch(`${baseUrl}/${id}`, configuraciones)
      .then((response) => {
        if (response.status == 403) {
          Swal.fire({
            title: "No tienes permiso para realizar esta acción",
            icon: "alert",
            confirmButtonColor: "#008000",
            confirmButtonText: "OK",
          });
          return response.json();
        }

        if (response.status == 404) {
          textoBuscarError.innerHTML += `<p><small>El paciente buscado no existe</small></p>`;
          return response.json();
        }

        if (response.ok) {
          return response.json();
        }
      })
      .then((data) => {
        console.log(data);
        renderizarPaciente(data);
      })
      .catch((error) => {
        console.log(error);
      });

    botonBuscarPorId.reset();
  });

  // BORRAR PACIENTE

  let formBorrar = document.querySelector(".eliminar-paciente");
  let textoEliminar = document.querySelector(".eliminar-paciente .texto-exito");
  let textoEliminarError = document.querySelector(
    ".eliminar-paciente .texto-error"
  );

  formBorrar.addEventListener("submit", function (e) {
    e.preventDefault();

    let dni = document.getElementById("id-eliminar-paciente").value;

    let res = confirm("¿Estas seguro que deseas eliminar el paciente?");
    if (res) {
      if (textoEliminar.hasChildNodes) {
        textoEliminar.innerHTML = "";
      }

      if (textoEliminarError.hasChildNodes) {
        textoEliminarError.innerHTML = "";
      }

      eliminarPaciente(dni);
    }

    formBorrar.reset();
  });

  function eliminarPaciente(id) {
    let settings = {
      method: "DELETE",
    };

    fetch(`${baseUrl}/${id}`, settings)
      .then((response) => {
        if (response.status == 403) {
          Swal.fire({
            title: "No tienes permiso para realizar esta acción",
            icon: "alert",
            confirmButtonColor: "#008000",
            confirmButtonText: "OK",
          });
          return response.json();
        }

        if (response.status == 404) {
          textoEliminarError.innerHTML += `<p><small>El paciente a eliminar no existe</small></p>`;
          return response.json();
        }

        if (response.ok) {
          textoEliminar.innerHTML += `<p><small>Paciente eliminado con éxito</small></p>`;
          return response.json();
        }
      })
      .then((data) => {
        console.log(data);
      })
      .catch((error) => {
        console.log(error);
      });
  }

  function renderizarPacientes(lista) {
    let divListar = document.getElementById("ul");

    if (divListar.hasChildNodes) {
      divListar.innerHTML = "";
    }

    lista.forEach((paciente) => {
      let nombrePaciente =
        paciente.firstName.charAt(0).toUpperCase() +
        paciente.firstName.slice(1).toLowerCase();
      let apellidoPaciente =
        paciente.lastName.charAt(0).toUpperCase() +
        paciente.lastName.slice(1).toLowerCase();

      divListar.innerHTML += `
                      <li class="list-style container-list">
                          <p> ${nombrePaciente}  ${apellidoPaciente} - Numero Dni: ${paciente.dni}</p>
                      </li>
                  `;
    });
  }

  function renderizarPaciente(paciente) {
    let divBuscar = document.querySelector(".form-admin-buscar");

    let nombrePaciente =
      paciente.firstName.charAt(0).toUpperCase() +
      paciente.firstName.slice(1).toLowerCase();
    let apellidoPaciente =
      paciente.lastName.charAt(0).toUpperCase() +
      paciente.lastName.slice(1).toLowerCase();

    divBuscar.innerHTML += `
        <div class="list-style container-list">
          <p> ${nombrePaciente}  ${apellidoPaciente} - Numero Dni: ${paciente.dni}</p>
        </div>
          `;
  }
});
