// Authentication utility functions
window.AuthUtils = {
    // Get access token from cookie
    getAccessToken() {
        const value = `; ${document.cookie}`;
        const parts = value.split(`; access token=`);
        if (parts.length === 2) return parts.pop().split(';').shift();
        return null;
    },

    // Get headers object with Authorization if token exists
    getAuthHeaders(additionalHeaders = {}) {
        const token = this.getAccessToken();
        const headers = { ...additionalHeaders };

        if (token) {
            headers['Authorization'] = `Bearer ${token}`;
        }

        return headers;
    },

    // Enhanced fetch with automatic auth header
    authenticatedFetch(url, options = {}) {
        const authHeaders = this.getAuthHeaders(options.headers);
        return fetch(url, {
            ...options,
            headers: authHeaders,
            credentials: options.credentials || 'include'
        });
    }
};