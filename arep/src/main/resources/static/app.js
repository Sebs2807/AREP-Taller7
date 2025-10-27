import { signIn, handleCallback, signOutRedirect } from "./auth.js";

const API_ROOT = "http://localhost:8080/hilos";

const hilosList = document.getElementById("hilos-list");
const postsList = document.getElementById("posts-list");
const postsPanelTitle = document.getElementById("posts-panel-title");
const postControls = document.getElementById("post-controls");
const createHiloForm = document.getElementById("create-hilo-form");
const createPostForm = document.getElementById("create-post-form");
const postContent = document.getElementById("post-content");
const charCount = document.getElementById("char-count");

let currentHiloId = null;

const loginBtn = document.getElementById("signIn");
const logoutBtn = document.getElementById("logout");
const userNameSpan = document.getElementById("user-name");

function getToken() {
    return localStorage.getItem("token");
}
function getAuthHeaders() {
    const t = getToken();
    return t ? { Authorization: "Bearer " + t } : {};
}

async function initAuth() {
    if (window.location.search.includes("code=")) {
        await handleCallback();
        window.history.replaceState({}, document.title, "/");
    }

    const token = getToken();
    if (token) {
        document.getElementById("auth-modal").classList.add("hidden");
        document.getElementById("app").classList.remove("hidden");
        userNameSpan.textContent = localStorage.getItem("username");
        fetchHilos();
    } else {
        document.getElementById("auth-modal").classList.remove("hidden");
        document.getElementById("app").classList.add("hidden");
        userNameSpan.textContent = "";
    }
}

loginBtn?.addEventListener("click", () => signIn());
logoutBtn?.addEventListener("click", () => {
    localStorage.removeItem("token");
    localStorage.removeItem("username");
    
    //const hostedUiDomain = "https://arep.auth.us-east-1.amazoncognito.com";
    //const clientId = "5ioimrqnbse75p8d0evu3c85n0";
    //const logoutUri = "https://minitwitter-camilo.s3.amazonaws.com/index.html";
    //window.location.href = `${hostedUiDomain}/logout?client_id=${clientId}&logout_uri=${encodeURIComponent(logoutUri)}`;
	window.location.href = "https://minitwitter-camilo.s3.amazonaws.com/index.html";
});

async function fetchHilos() {
    const res = await fetch(API_ROOT, { headers: getAuthHeaders() });
    const hilos = await res.json();
    renderHilos(hilos);
}

function renderHilos(hilos) {
    hilosList.innerHTML = "";
    hilos.forEach(h => {
        const li = document.createElement("li");
        li.className = "hilo";
        li.textContent = h.title || "(sin título)";
        li.addEventListener("click", () => selectHilo(h));
        hilosList.appendChild(li);
    });
}

function selectHilo(hilo) {
    currentHiloId = hilo.id;
    postsPanelTitle.textContent = hilo.title || "Posts";
    postControls.classList.remove("hidden");
    fetchPosts(hilo.id);
}

async function fetchPosts(hiloId) {
    postsList.innerHTML = "<li>cargando...</li>";
    const res = await fetch(`${API_ROOT}/${hiloId}/posts`, { headers: getAuthHeaders() });
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
    posts.forEach(p => {
        const li = document.createElement("li");
        li.className = "post";
        // Mostrar el correo si está disponible, si no el username, si no "anon"
        const userLabel = p.author?.email || p.author?.username || "anon";
        li.innerHTML = `<div class="meta">${userLabel} • ${new Date(p.createdAt).toLocaleString()}</div>
                        <div class="content">${escapeHtml(p.content)}</div>`;
        postsList.appendChild(li);
    });
}

function escapeHtml(s) {
    if (!s) return "";
    return s.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
}


createHiloForm.addEventListener("submit", async e => {
    e.preventDefault();
    const title = document.getElementById("hilo-title").value.trim();
    if (!title) return alert("El título es requerido");

    const res = await fetch(API_ROOT, {
        method: "POST",
        headers: { "Content-Type": "application/json", ...getAuthHeaders() },
        body: JSON.stringify({ title })
    });

    if (res.ok) {
        document.getElementById("hilo-title").value = "";
        fetchHilos();
    } else if (res.status === 401) {
        alert("Debes iniciar sesión");
    } else {
        alert("Error creando hilo: " + await res.text());
    }
});


postContent.addEventListener("input", () => {
    const len = postContent.value.length;
    charCount.textContent = `${len}/140`;
    charCount.style.color = len > 140 ? "red" : "#374151";
});

createPostForm.addEventListener("submit", async e => {
    e.preventDefault();
    if (!currentHiloId) return alert("Seleccione un hilo");

    const content = postContent.value.trim();
    if (!content) return alert("El contenido no puede estar vacío");
    if (content.length > 140) return alert("El post debe tener máximo 140 caracteres");

    const res = await fetch(`${API_ROOT}/${currentHiloId}/posts`, {
        method: "POST",
        headers: { "Content-Type": "application/json", ...getAuthHeaders() },
        body: JSON.stringify({ content })
    });

    if (res.ok) {
        postContent.value = "";
        charCount.textContent = "0/140";
        fetchPosts(currentHiloId);
    } else if (res.status === 401) {
        alert("Debes iniciar sesión");
    } else {
        alert("Error creando post: " + await res.text());
    }
});


initAuth();
