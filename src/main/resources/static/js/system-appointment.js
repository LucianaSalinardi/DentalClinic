window.addEventListener("load", function () {
  const baseUrl = "/clinical-dental/appointments";
  const textoErrorListado = document.querySelector(".texto-error-listado");
  const tabla = document.querySelector("table");

  //CREAR TURNO

  let formCrearTurno = document.querySelector(".form-crear");
  formCrearTurno.addEventListener("submit", function (event) {
    event.preventDefault();

    let textoCrear = document.querySelector(".form-crear .texto-exito");
    let textoError = document.querySelector(".form-crear .texto-error");

    const inputIdPaciente = document.getElementById("id-paciente").value;
    const inputIdOdontologo = document.getElementById("id-odontologo").value;
    const inputFecha = document.getElementById("alta-turno-dia").value;
    const inputHora = document.getElementById("alta-turno-hora").value;

      if (textoCrear.hasChildNodes) {
        textoCrear.innerHTML = "";
      }

      if (textoError.hasChildNodes) {
        textoError.innerHTML = "";
      }

      const datosTurno = {
        patient: {
          idPatient: inputIdPaciente,
        },
        dentist: {
          idDentist: inputIdOdontologo,
        },
        appointmentDate: inputFecha,
        appointmentHour: inputHora,
      };

      const configuraciones = {
        method: "POST",
        headers: {
          "Content-type": "application/json",
        },
        body: JSON.stringify(datosTurno),
      };

      fetch(baseUrl, configuraciones)
        .then((response) => {
          if (response.status == 400) {
            textoError.innerHTML += `<p><small>Revisa los campos ingresados</small></p>`;
            return response.json();
          }

          if (response.status == 404) {
            textoError.innerHTML += `<p><small>No existe el paciente u odontologo </small></p>`;
            return response.json();
          }

          if (response.status == 409) {
            textoError.innerHTML += `<p><small>Ya existe un turno</small></p>`;
            return response.json(); 
          }

          if (response.ok) {
            textoCrear.innerHTML += `<p><small>Turno creado con éxito</small></p>`;
            return response.json();
          }
        })
        .then((data) => {
          console.log(data);

        })
        .catch((error) => {
          console.log(error);
        });

      formCrearTurno.reset();
    
  });

  //ACTUALIZAR TURNO

  let formActualizar = document.querySelector(".form-actualizar");
  let textoActualizar = document.querySelector(".form-actualizar .texto-exito");
  let textoErrorActualizar = document.querySelector(
    ".form-actualizar .texto-error"
  );

  formActualizar.addEventListener("submit", function (event) {
    event.preventDefault();

    const idTurno = document.getElementById("id-turno-actualizar").value;
    const idPacienteActualizar = document.getElementById(
      "id-paciente-actualizar"
    ).value;
    const idOdontologoActualizar = document.getElementById(
      "id-odontologo-actualizar"
    ).value;
    const actualizarFecha = document.getElementById(
      "actualizar-turno-dia"
    ).value;
    const actualizarHora = document.getElementById(
      "actualizar-turno-hora"
    ).value;

      if (textoActualizar.hasChildNodes) {
        textoActualizar.innerHTML = "";
      }

      if (textoErrorActualizar.hasChildNodes) {
        textoErrorActualizar.innerHTML = "";
      }

      const datosTurno = {
        idAppointment: idTurno,
        patient: {
          idPatient: idPacienteActualizar,
        },
        dentist: {
          idDentist: idOdontologoActualizar,
        },
        appointmentDate: actualizarFecha,
        appointmentHour: actualizarHora,
      };

      const configuraciones = {
        method: "PUT",
        headers: {
          "Content-type": "application/json",
        },
        body: JSON.stringify(datosTurno),
      };

      fetch(baseUrl, configuraciones)
        .then((response) => {

          if (response.status == 400) {
            textoErrorActualizar.innerHTML += `<p><small>Revisa los campos ingresados</small></p>`;
            return response.json(); 
          }

          if (response.status == 404) {
            textoErrorActualizar.innerHTML += `<p><small>No existe turno asociado a esos datos</small></p>`;
            return response.json();
          }

          
          if (response.status == 409) {
            textoErrorActualizar.innerHTML += `<p><small>Ya tiene un turno para esa fecha</small></p>`;
            return response.json();
          }

          if (response.ok) {
            textoActualizar.innerHTML += `<p><small>Turno actualizado con éxito</small></p>`;
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

  //LISTAR TODOS

  let botonListarTodos = document.querySelector(".listar-todos button");

  botonListarTodos.addEventListener("click", function (event) {
    event.preventDefault();

    if (tabla.hasChildNodes) {
      tabla.innerHTML = "";
    }

    if (textoErrorListado.hasChildNodes) {
      textoErrorListado.innerHTML = "";
    }

    const configuraciones = {
      method: "GET",
      headers: {},
    };

    fetch(baseUrl, configuraciones)
      .then((response) => {
        if (response.status == 404) {
          textoErrorListado.innerHTML += `<span class="container-list"> No existen turnos asignados</span>`;
        }

        return response.json();
      })
      .then((data) => {
        renderizarTurnos(data);
      })
      .catch((error) => console.log(error));
  });

  //LISTAR TURNOS POR ID PACIENTE

  let botonListarTurnoPaciente = document.querySelector(
    ".listar-paciente form"
  );

  botonListarTurnoPaciente.addEventListener("submit", function (event) {
    event.preventDefault();

    const idPaciente = document.getElementById("id-turno-paciente").value;

    if (tabla.hasChildNodes) {
      tabla.innerHTML = "";
    }

    if (textoErrorListado.hasChildNodes) {
      textoErrorListado.innerHTML = "";
    }

    const configuraciones = {
      method: "GET",
      headers: {},
    };

    fetch(`${baseUrl}/patients/${idPaciente}`, configuraciones)
      .then((response) => {
        if (response.status == 404) {
          textoErrorListado.innerHTML += `<span class="container-list"> No existen turnos asignados o el paciente no existe </span>`;
          return response.json();
        }
        if(response.ok){
          return response.json();
        }
  })
      .then((data) => {
        renderizarTurnos(data);
      })
      .catch((error) => {
        console.log(error);
      });
    botonListarTurnoPaciente.reset();
  });

  //LISTAR TURNOS POR ID ODONTOLOGO

  let botonListarTurnoOdontologo = document.querySelector(
    ".listar-odontologo form"
  );

  botonListarTurnoOdontologo.addEventListener("submit", function (event) {
    event.preventDefault();

    const idOdontologo = document.getElementById("id-turno-odontologo").value;

    if (tabla.hasChildNodes) {
      tabla.innerHTML = "";
    }

    if (textoErrorListado.hasChildNodes) {
      textoErrorListado.innerHTML = "";
    }

    const configuraciones = {
      method: "GET",
      headers: {},
    };

    fetch(`${baseUrl}/dentists/${idOdontologo}`, configuraciones)
      .then((response) => {
        if (response.status == 404) {
          textoErrorListado.innerHTML += `<span class="container-list"> No existen turnos asignados o el odontólogo no existe </span>`;
        }

        return response.json();
      })
      .then((data) => {
        renderizarTurnos(data);
      })
      .catch((error) => {
        console.log(error);
      });
      botonListarTurnoOdontologo.reset();
    });
  
  

  // BUSCAR POR ID

  let botonBuscarPorId = document.querySelector(
    ".form-admin-buscar-turno form"
  );
  let textoBuscarError = document.querySelector(
    ".form-admin-buscar-turno div"
  );

  botonBuscarPorId.addEventListener("submit", function (event) {
    event.preventDefault();

    const id = document.getElementById("id-turno").value;

    if (textoBuscarError.hasChildNodes) {
      textoBuscarError.innerHTML = "";
    }

    const configuraciones = {
      method: "GET",
      headers: {},
    };

    fetch(`${baseUrl}/${id}`, configuraciones)
      .then((response) => {

        if (response.status == 404) {
          textoBuscarError.innerHTML += `<p><small>El turno buscado no existe</small></p>`;
  
        }

        if (response.ok) {
          return response.json();
        }
      })
      .then((data) => {
        console.log(data);
        renderizarTurno(data);
      })
      .catch((error) => {
        console.log(error);
      });

    botonBuscarPorId.reset();
  });

  // ELIMINAR TURNO

  let formBorrar = document.querySelector(".eliminar-turno");
  let textoEliminar = document.querySelector(".eliminar-turno .texto-exito");
  let textoErrorEliminar = document.querySelector(
    ".eliminar-turno .texto-error"
  );

  formBorrar.addEventListener("submit", function (e) {
    e.preventDefault();

    let idTurno = document.getElementById("id-turno-eliminar").value;

    if (idTurno != null) {
      let res = confirm("¿Estas seguro que deseas eliminar el turno?");
      if (res) {
        if (textoEliminar.hasChildNodes) {
          textoEliminar.innerHTML = "";
        }

        if (textoErrorEliminar.hasChildNodes) {
          textoErrorEliminar.innerHTML = "";
        }
        eliminarTurno(idTurno);
      }

      formBorrar.reset();
    }
  });

  function eliminarTurno(id) {
    let settings = {
      method: "DELETE",
    };

    fetch(`${baseUrl}/${id}`, settings)
      .then((response) => {
        if (response.status == 404) {
          textoErrorEliminar.innerHTML += `<p><small>El turno a eliminar no existe </small></p>`;
          return response.json();
        }

        if (response.ok) {
          return response.json();
        }
      })
      .then((data) => {
        console.log(data);

        if (data != null) {
          textoEliminar.innerHTML += `<p><small>Turno eliminado con éxito</small></p>`;
        }
      })
      .catch((error) => {
        console.log(error);
      });
  }

  function renderizarTurno(turno) {
    let divBuscar = document.querySelector(".form-admin-buscar");

    let fechaDia = new Date(turno.appointmentDate);
    let nombrePaciente =
      turno.patient.firstName.charAt(0).toUpperCase() +
      turno.patient.firstName.slice(1).toLowerCase();
    let apellidoPaciente =
      turno.patient.lastName.charAt(0).toUpperCase() +
      turno.patient.lastName.slice(1).toLowerCase();

    let nombreOdontologo =
      turno.dentist.firstName.charAt(0).toUpperCase() +
      turno.dentist.firstName.slice(1).toLowerCase();
    let apellidoOdontologo =
      turno.dentist.lastName.charAt(0).toUpperCase() +
      turno.dentist.lastName.slice(1).toLowerCase();

    divBuscar.innerHTML += `
        <div class="list-style container-list">
          <p> Paciente:  ${nombrePaciente}  ${apellidoPaciente} 
          - Odontólogo:  ${nombreOdontologo}  ${apellidoOdontologo} 
          - Fecha Turno: ${fechaDia.toLocaleString()}</p>
        </div>
          `;
  }
});

function renderizarTurnos(lista) {
  const tablaTurnos = document.querySelector("table");
  tablaTurnos.innerHTML += `<thead>
        <tr>
          <th>Paciente</th>
          <th>Odontólogo</th>
          <th>Fecha</th>
        </tr>
      </thead>`;

  lista.forEach((turno) => {
    let nombrePaciente =
      turno.patient.firstName.charAt(0).toUpperCase() +
      turno.patient.firstName.slice(1).toLowerCase();
    let apellidoPaciente =
      turno.patient.lastName.charAt(0).toUpperCase() +
      turno.patient.lastName.slice(1).toLowerCase();
    let fechaDia = new Date(turno.appointmentDate);

    let nombreOdontologo =
      turno.dentist.firstName.charAt(0).toUpperCase() +
      turno.dentist.firstName.slice(1).toLowerCase();
    let apellidoOdontologo =
      turno.dentist.lastName.charAt(0).toUpperCase() +
      turno.dentist.lastName.slice(1).toLowerCase();

    tablaTurnos.innerHTML += `<tbody>
                    <tr>
                      <td> ${nombrePaciente}  ${apellidoPaciente}</td>
                      <td> ${nombreOdontologo}  ${apellidoOdontologo}</td>
                      <td>${fechaDia.toLocaleString()}</td>
                    </tr>
                  </tbody>`;
  });
}
