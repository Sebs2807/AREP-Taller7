const API_ROOT = "/hilos";
const AUTH_ROOT = "/auth";

const hilosList = document.getElementById("hilos-list");
const postsList = document.getElementById("posts-list");
const postsPanelTitle = document.getElementById("posts-panel-title");
const postControls = document.getElementById("post-controls");
const createHiloForm = document.getElementById("create-hilo-form");
const createPostForm = document.getElementById("create-post-form");
const postContent = document.getElementById("post-content");
const charCount = document.getElementById("char-count");

let currentHiloId = null;

const loginForm = document.getElementById("login-form");
const registerForm = document.getElementById("register-form");
const userInfo = document.getElementById("user-info");
const userNameSpan = document.getElementById("user-name");
const logoutBtn = document.getElementById("logout");

function getToken() {
	return localStorage.getItem("token");
}
function setToken(t) {
	if (t) localStorage.setItem("token", t);
	else localStorage.removeItem("token");
}
function getAuthHeaders() {
	const t = getToken();
	return t ? { Authorization: "Bearer " + t } : {};
}

function updateAuthUI() {
	const t = getToken();
	if (t) {
		loginForm.classList.add("hidden");
		registerForm.classList.add("hidden");
		userInfo.classList.remove("hidden");
		userNameSpan.textContent = localStorage.getItem("username") || "";
	} else {
		loginForm.classList.remove("hidden");
		registerForm.classList.remove("hidden");
		userInfo.classList.add("hidden");
		userNameSpan.textContent = "";
	}
}

logoutBtn?.addEventListener("click", () => {
	setToken(null);
	localStorage.removeItem("username");
	updateAuthUI();
});

async function fetchHilos() {
	const res = await fetch(API_ROOT);
	const hilos = await res.json();
	renderHilos(hilos);
}

function renderHilos(hilos) {
	hilosList.innerHTML = "";
	hilos.forEach((h) => {
		const li = document.createElement("li");
		li.className = "hilo";
		li.textContent = `${h.title || "(sin título)"}`;
		li.addEventListener("click", () => selectHilo(h));
		hilosList.appendChild(li);
	});
}

function selectHilo(hilo) {
	currentHiloId = hilo.id;
	postsPanelTitle.textContent = `${hilo.title ? hilo.title : "Posts"}`;
	postControls.classList.remove("hidden");
	fetchPosts(hilo.id);
}

async function fetchPosts(hiloId) {
	postsList.innerHTML = "<li>cargando...</li>";
	const res = await fetch(`${API_ROOT}/${hiloId}/posts`);
	if (!res.ok) {
		postsList.innerHTML = "<li>Error al cargar posts</li>";
		return;
	}
	const posts = await res.json();
	renderPosts(posts);
}

function renderPosts(posts) {
	postsList.innerHTML = "";
	if (!posts.length) {
		postsList.innerHTML = "<li>No hay posts en este hilo</li>";
		return;
	}
	posts.forEach((p) => {
		const li = document.createElement("li");
		li.className = "post";
		li.innerHTML = `<div class="meta">${
			p.author?.username || "anon"
		} • ${new Date(p.createdAt).toLocaleString()}</div>
                    <div class="content">${escapeHtml(p.content)}</div>`;
		postsList.appendChild(li);
	});
}

function escapeHtml(s) {
	if (!s) return "";
	return s
		.replaceAll("&", "&amp;")
		.replaceAll("<", "&lt;")
		.replaceAll(">", "&gt;");
}

createHiloForm.addEventListener("submit", async (e) => {
	e.preventDefault();
	const title = document.getElementById("hilo-title").value.trim();
	if (!title) return alert("El título es requerido");
	const payload = { title };
	const headers = { "Content-Type": "application/json", ...getAuthHeaders() };
	const res = await fetch(API_ROOT, {
		method: "POST",
		headers,
		body: JSON.stringify(payload),
	});
	if (res.ok) {
		document.getElementById("hilo-title").value = "";
		fetchHilos();
	} else if (res.status === 401) {
		alert("Debes iniciar sesión");
	} else {
		const txt = await res.text();
		alert("Error creando hilo: " + txt);
	}
});

postContent.addEventListener("input", () => {
	const len = postContent.value.length;
	charCount.textContent = `${len}/140`;
	if (len > 140) charCount.style.color = "red";
	else charCount.style.color = "#374151";
});

createPostForm.addEventListener("submit", async (e) => {
	e.preventDefault();
	if (!currentHiloId) return alert("Seleccione un hilo");
	const content = postContent.value.trim();
	if (!content) return alert("El contenido no puede estar vacío");
	if (content.length > 140)
		return alert("El post debe tener máximo 140 caracteres");

	const params = new URLSearchParams({ content });
	const headers = { ...getAuthHeaders() };
	const res = await fetch(
		`${API_ROOT}/${currentHiloId}/posts?${params.toString()}`,
		{ method: "POST", headers }
	);
	if (res.ok) {
		postContent.value = "";
		charCount.textContent = "0/140";
		fetchPosts(currentHiloId);
	} else if (res.status === 401) {
		alert("Debes iniciar sesión");
	} else {
		const txt = await res.text();
		alert("Error creando post: " + txt);
	}
});

// auth handlers
loginForm?.addEventListener("submit", async (e) => {
	e.preventDefault();
	const username = document.getElementById("login-username").value.trim();
	const password = document.getElementById("login-password").value;
	if (!username || !password) return alert("Username/password required");
	const res = await fetch(AUTH_ROOT + "/login", {
		method: "POST",
		headers: { "Content-Type": "application/json" },
		body: JSON.stringify({ username, password }),
	});
	if (res.ok) {
		const body = await res.json();
		setToken(body.token);
		localStorage.setItem("username", body.username);
		updateAuthUI();
	} else {
		alert("Login failed");
	}
});

registerForm?.addEventListener("submit", async (e) => {
	e.preventDefault();
	const username = document.getElementById("reg-username").value.trim();
	const password = document.getElementById("reg-password").value;
	if (!username || !password) return alert("Username/password required");
	const res = await fetch(AUTH_ROOT + "/register", {
		method: "POST",
		headers: { "Content-Type": "application/json" },
		body: JSON.stringify({ username, password }),
	});
	if (res.ok) {
		alert("Usuario creado, ahora inicia sesión");
		document.getElementById("reg-username").value = "";
		document.getElementById("reg-password").value = "";
	} else {
		const txt = await res.text();
		alert("Error registro: " + txt);
	}
});

updateAuthUI();
fetchHilos();
