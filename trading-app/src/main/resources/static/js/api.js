/**
 * API Client for communicating with the Trading App backend.
 * Handles all HTTP requests and responses.
 */

const API_BASE_URL = '/api';

/**
 * Generic fetch wrapper with error handling.
 */
async function apiCall(endpoint, options = {}) {
    const url = `${API_BASE_URL}${endpoint}`;
    
    // Set default headers
    const headers = {
        'Content-Type': 'application/json',
        ...options.headers
    };
    
    try {
        const response = await fetch(url, {
            ...options,
            headers
        });
        
        // Handle non-OK responses
        if (!response.ok) {
            const error = await response.json().catch(() => ({}));
            throw {
                status: response.status,
                code: error.code || 'HTTP_ERROR',
                message: error.message || 'HTTP Error: ' + response.status
            };
        }
        
        // Parse and return JSON (if any)
        const contentType = response.headers.get('content-type');
        if (contentType && contentType.includes('application/json')) {
            return await response.json();
        }
        return response;
        
    } catch (error) {
        console.error('API call error:', error);
        throw error;
    }
}

/**
 * Authentication API endpoints
 */
const AuthAPI = {
    /**
     * Get the Kite login URL for OAuth authentication.
     */
    getLoginURL: async () => {
        return apiCall('/auth/login-url', { method: 'GET' });
    },
    
    /**
     * Generate session by exchanging request token for access token.
     */
    generateSession: async (requestToken) => {
        return apiCall('/auth/session', {
            method: 'POST',
            body: JSON.stringify({ requestToken })
        });
    },
    
    /**
     * Get current session details.
     */
    getSession: async () => {
        return apiCall('/auth/session', { method: 'GET' });
    },
    
    /**
     * Logout the current user.
     */
    logout: async () => {
        return apiCall('/auth/logout', { method: 'POST' });
    }
};

/**
 * Orders API endpoints (to be implemented in Phase 2)
 */
const OrdersAPI = {
    /**
     * Get all orders for the current user.
     */
    getOrders: async () => {
        return apiCall('/orders', { method: 'GET' });
    },
    
    /**
     * Place a new order.
     */
    placeOrder: async (orderData) => {
        return apiCall('/orders', {
            method: 'POST',
            body: JSON.stringify(orderData)
        });
    },
    
    /**
     * Modify an existing order.
     */
    modifyOrder: async (orderId, orderData) => {
        return apiCall(`/orders/${orderId}`, {
            method: 'PUT',
            body: JSON.stringify(orderData)
        });
    },
    
    /**
     * Cancel an order.
     */
    cancelOrder: async (orderId) => {
        return apiCall(`/orders/${orderId}`, { method: 'DELETE' });
    }
};

/**
 * Portfolio API endpoints (to be implemented in Phase 2)
 */
const PortfolioAPI = {
    /**
     * Get user holdings.
     */
    getHoldings: async () => {
        return apiCall('/portfolio/holdings', { method: 'GET' });
    },
    
    /**
     * Get user positions.
     */
    getPositions: async () => {
        return apiCall('/portfolio/positions', { method: 'GET' });
    },
    
    /**
     * Get user margins and account balance.
     */
    getMargins: async () => {
        return apiCall('/portfolio/margins', { method: 'GET' });
    },
    
    /**
     * Get user profile.
     */
    getProfile: async () => {
        return apiCall('/portfolio/profile', { method: 'GET' });
    }
};

/**
 * Market Data API endpoints (to be implemented in Phase 2)
 */
const MarketDataAPI = {
    /**
     * Get quotes for given symbols.
     */
    getQuotes: async (symbols) => {
        const symbolsParam = Array.isArray(symbols) ? symbols.join(',') : symbols;
        return apiCall(`/market/quotes?symbols=${symbolsParam}`, { method: 'GET' });
    },
    
    /**
     * Get historical OHLC data.
     */
    getHistoricalData: async (instrumentToken, interval, from, to) => {
        const params = new URLSearchParams({
            instrumentToken,
            interval,
            from,
            to
        });
        return apiCall(`/market/historical?${params.toString()}`, { method: 'GET' });
    },
    
    /**
     * Get all instruments.
     */
    getInstruments: async () => {
        return apiCall('/market/instruments', { method: 'GET' });
    },

    getInstrumentTokens: async (symbols) => {
        const symbolsParam = Array.isArray(symbols) ? symbols.join(',') : symbols;
        return apiCall(`/market/tokens?symbols=${symbolsParam}`, { method: 'GET' });
    }
};

/**
 * Margin Calculator API endpoints (to be implemented in Phase 2)
 */
const MarginAPI = {
    /**
     * Calculate margin requirement for an order.
     */
    calculateMargin: async (orderData) => {
        return apiCall('/margin/calculate', {
            method: 'POST',
            body: JSON.stringify(orderData)
        });
    }
};

/**
 * Bot API endpoints (MVP foundation)
 */
const BotAPI = {
    start: async (config) => {
        return apiCall('/bot/start', {
            method: 'POST',
            body: JSON.stringify(config)
        });
    },

    stop: async (botId) => {
        return apiCall(`/bot/stop/${botId}`, {
            method: 'POST'
        });
    },

    status: async () => {
        return apiCall('/bot/status', { method: 'GET' });
    },

    logs: async () => {
        return apiCall('/bot/logs', { method: 'GET' });
    }
};

/**
 * Utility function to handle API errors and display user-friendly messages.
 */
function handleApiError(error) {
    console.error('API Error:', error);
    
    let message = 'An unexpected error occurred';
    
    if (error.message) {
        message = error.message;
    } else if (error.status === 401) {
        message = 'Unauthorized. Please login again.';
        // Redirect to login
        window.location.href = '/';
    } else if (error.status === 403) {
        message = 'Access denied.';
    } else if (error.status === 404) {
        message = 'Resource not found.';
    } else if (error.status >= 500) {
        message = 'Server error. Please try again later.';
    }
    
    return message;
}

/**
 * Utility function to show a toast notification
 */
function showNotification(message, type = 'info', duration = 5000) {
    // Create notification element
    const notification = document.createElement('div');
    notification.className = `alert alert-${type}`;
    notification.textContent = message;
    notification.style.position = 'fixed';
    notification.style.top = '20px';
    notification.style.right = '20px';
    notification.style.zIndex = '9999';
    notification.style.maxWidth = '400px';
    
    document.body.appendChild(notification);
    
    // Auto-remove after duration
    setTimeout(() => {
        notification.remove();
    }, duration);
}
