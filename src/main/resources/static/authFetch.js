// === authFetch.js ===

// Получаем токен из localStorage
function getToken() {
    return localStorage.getItem("token");
}

// Универсальная функция для запросов с авторизацией
async function authFetch(url, options = {}) {
    const token = getToken();

    // Добавляем заголовок Authorization
    const headers = {
        ...(options.headers || {}),
        "Content-Type": options.headers?.["Content-Type"] || "application/json",
        "Authorization": `Bearer ${token}`
    };

    // Отправляем запрос
    const response = await fetch(url, { ...options, headers });

    // Проверяем авторизацию
    if (response.status === 401) {
        console.warn("🚫 Токен недействителен или отсутствует. Перенаправление на логин...");
        localStorage.removeItem("token");
        window.location.href = "/login.html";
        return null;
    }

    // Пытаемся вернуть JSON
    try {
        return await response.json();
    } catch {
        return response;
    }
}
