window.addEventListener("load", function () {
  const baseUrl = "/clinical-dental/dentists";

  let formCrearOdontologo = document.querySelector(".form-crear");

  //CREAR ODONTOLOGO

  formCrearOdontologo.addEventListener("submit", function (event) {
    event.preventDefault();

    let textoCrear = document.querySelector(".form-crear .texto-exito");
    let textoCrearError = document.querySelector(".form-crear .texto-error");

    const inputNombreOdontologo = document.getElementById("crear-nombre").value;
    const inputApellidoOdontologo =
      document.getElementById("crear-apellido").value;
    const inputMatricula = document.getElementById("crear-matricula").value;

    if (textoCrear.hasChildNodes) {
      textoCrear.innerHTML = "";
    }

    if (textoCrearError.hasChildNodes) {
      textoCrearError.innerHTML = "";
    }

    const datosOdontologo = {
      enrollment: inputMatricula,
      firstName: inputNombreOdontologo,
      lastName: inputApellidoOdontologo,
    };

    const configuraciones = {
      method: "POST",
      headers: {
        "Content-type": "application/json",
      },
      body: JSON.stringify(datosOdontologo),
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
          textoCrearError.innerHTML += `<p><small>El odontologo ya existe</small></p>`;
          return response.json();
        }

        if (response.ok) {
          textoCrear.innerHTML += `<p><small>Odontólogo creado con éxito</small></p>`;
          return response.json();
        }
      })
      .then((data) => {
        console.log(data);
      })
      .catch((error) => {
        console.log(error);
      });

    formCrearOdontologo.reset();
  });

  //ACTUALIZAR ODONTOLOGO

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
    const inputActualizarMatricula = document.getElementById(
      "actualizar-matricula"
    ).value;

    if (textoActualizar.hasChildNodes) {
      textoActualizar.innerHTML = "";
    }

    if (textoActualizarError.hasChildNodes) {
      textoActualizarError.innerHTML = "";
    }

    const datosOdontologo = {
      idDentist: idActualizar,
      enrollment: inputActualizarMatricula,
      firstName: inputActualizarNombre,
      lastName: inputActualizarApellido,
    };

    const configuraciones = {
      method: "PUT",
      headers: {
        "Content-type": "application/json",
      },
      body: JSON.stringify(datosOdontologo),
    };

    fetch(baseUrl, configuraciones)
      .then((response) => {
        if (response.status == 400) {
          textoActualizarError.innerHTML += `<p><small>Revisa los campos ingresados</small></p>`;
          return response.json();
        }

        if (response.status == 404) {
          textoActualizarError.innerHTML += `<p><small>El odontologo no existe</small></p>`;
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

        if (response.ok) {
          textoActualizar.innerHTML += `<p><small>Odontólogo actualizado con éxito</small></p>`;
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

  //LISTAR TODOS LOS ODONTOLOGOS

  let botonListarTodos = document.querySelector(".listar-o button");

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
        renderizarOdontologos(data);
      })
      .catch((error) => console.log(error));
  });

  // BUSCAR POR ID

  let botonBuscarPorId = document.querySelector(".buscar-odontologo");
  let textoBuscarError = document.querySelector(
    ".buscar-odontologo .texto-error"
  );

  botonBuscarPorId.addEventListener("submit", function (event) {
    event.preventDefault();

    const id = document.getElementById("id-odontologo").value;

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
          textoBuscarError.innerHTML += `<p><small>El odontologo buscado no existe</small></p>`;
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

        if (response.ok) {
          return response.json();
        }
      })
      .then((data) => {
        console.log(data);
        renderizarOdontologo(data);
      })
      .catch((error) => {
        console.log(error);
      });

    botonBuscarPorId.reset();
  });

  // BORRAR ODONTOLOGO

  let formBorrar = document.querySelector(".eliminar-odontologo");
  let textoEliminar = document.querySelector(
    ".eliminar-odontologo .texto-exito"
  );
  let textoEliminarError = document.querySelector(
    ".eliminar-odontologo .texto-error"
  );

  formBorrar.addEventListener("submit", function (e) {
    e.preventDefault();

    let idEliminar = document.getElementById("id-eliminar-odontologo").value;

    let res = confirm("¿Estas seguro que deseas eliminar el odontólogo?");
    if (res) {
      if (textoEliminar.hasChildNodes) {
        textoEliminar.innerHTML = "";
      }

      if (textoEliminarError.hasChildNodes) {
        textoEliminarError.innerHTML = "";
      }

      eliminarOdontologo(idEliminar);
    }

    formBorrar.reset();
  });

  function eliminarOdontologo(id) {
    let settings = {
      method: "DELETE",
    };

    fetch(`${baseUrl}/${id}`, settings)
      .then((response) => {
        if (response.status == 404) {
          textoEliminarError.innerHTML += `<p><small>El odontologo a eliminar no existe</small></p>`;
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

        if (response.ok) {
          textoEliminar.innerHTML += `<p><small>Odontólogo eliminado con éxito</small></p>`;
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

  function renderizarOdontologos(lista) {
    let divListar = document.getElementById("ul");

    if (divListar.hasChildNodes) {
      divListar.innerHTML = "";
    }

    lista.forEach((odontologo) => {
      let nombreOdontologo =
        odontologo.firstName.charAt(0).toUpperCase() +
        odontologo.firstName.slice(1).toLowerCase();
      let apellidoOdontologo =
        odontologo.lastName.charAt(0).toUpperCase() +
        odontologo.lastName.slice(1).toLowerCase();

      divListar.innerHTML += `
                    <li class="list-style container-list">
                        <p> ${nombreOdontologo}  ${apellidoOdontologo} - Numero Matrícula: ${odontologo.enrollment}</p>
                    </li>
                `;
    });
  }

  function renderizarOdontologo(odontologo) {
    let divBuscar = document.querySelector(".form-admin-buscar");

    let nombreOdontologo =
      odontologo.firstName.charAt(0).toUpperCase() +
      odontologo.firstName.slice(1).toLowerCase();
    let apellidoOdontologo =
      odontologo.lastName.charAt(0).toUpperCase() +
      odontologo.lastName.slice(1).toLowerCase();

    divBuscar.innerHTML += `
      <div class="list-style container-list">
        <p> ${nombreOdontologo}  ${apellidoOdontologo} - Numero Matrícula: ${odontologo.enrollment}</p>
      </div>
        `;
  }
});
