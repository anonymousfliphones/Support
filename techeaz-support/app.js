// Techeaz Support App
class TecheazSupport {
    constructor() {
        this.data = null;
        this.filteredFAQs = [];
        this.currentCategory = 'all';
        this.searchQuery = '';
        this.init();
    }

    async init() {
        try {
            await this.loadData();
            this.renderApp();
            this.setupEventListeners();
            this.hideLoading();
        } catch (error) {
            console.error('Failed to initialize app:', error);
            this.showError();
        }
    }

    async loadData() {
        try {
            // For development, load from local file
            // For production, this would be the GitHub raw URL
            const response = await fetch('./content.json');
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            this.data = await response.json();
            this.filteredFAQs = this.data.faqs;
        } catch (error) {
            console.error('Error loading data:', error);
            // Fallback to minimal data structure
            this.data = {
                app: {
                    name: "Techeaz Support",
                    description: "Get help and find answers to frequently asked questions",
                    version: "1.0.0"
                },
                contact: {
                    email: "support@techeaz.com",
                    phone: "+1-555-TECHEAZ",
                    hours: "Monday - Friday: 9:00 AM - 6:00 PM EST",
                    response_time: "We typically respond within 24 hours"
                },
                categories: [],
                faqs: [],
                announcements: [],
                quick_actions: []
            };
            this.filteredFAQs = [];
        }
    }

    renderApp() {
        this.renderAppInfo();
        this.renderAnnouncements();
        this.renderQuickActions();
        this.renderCategories();
        this.renderFAQs();
        this.renderContactInfo();
    }

    renderAppInfo() {
        document.getElementById('app-name').textContent = this.data.app.name;
        document.getElementById('app-description').textContent = this.data.app.description;
        document.getElementById('app-version').textContent = `v${this.data.app.version}`;
        document.title = this.data.app.name;
    }

    renderAnnouncements() {
        const container = document.getElementById('announcements-container');
        const section = document.getElementById('announcements-section');
        
        const activeAnnouncements = this.data.announcements.filter(ann => ann.active);
        
        if (activeAnnouncements.length === 0) {
            section.style.display = 'none';
            return;
        }

        section.style.display = 'block';
        container.innerHTML = activeAnnouncements.map(announcement => `
            <div class="announcement announcement-${announcement.type}">
                <div class="announcement-content">
                    <h4>${announcement.title}</h4>
                    <p>${announcement.message}</p>
                    <small>Posted: ${new Date(announcement.date).toLocaleDateString()}</small>
                </div>
            </div>
        `).join('');
    }

    renderQuickActions() {
        const container = document.getElementById('quick-actions-container');
        
        container.innerHTML = this.data.quick_actions.map(action => `
            <div class="quick-action-card" onclick="this.handleQuickAction('${action.action}')">
                <div class="quick-action-icon">${action.icon}</div>
                <h4>${action.title}</h4>
                <p>${action.description}</p>
            </div>
        `).join('');
    }

    renderCategories() {
        const container = document.getElementById('category-filter');
        const allButton = container.querySelector('[data-category="all"]');
        
        // Clear existing category buttons (except "All Categories")
        const existingButtons = container.querySelectorAll('.category-btn:not([data-category="all"])');
        existingButtons.forEach(btn => btn.remove());
        
        // Add category buttons
        this.data.categories.forEach(category => {
            const button = document.createElement('button');
            button.className = 'category-btn';
            button.setAttribute('data-category', category.id);
            button.innerHTML = `${category.icon} ${category.name}`;
            container.appendChild(button);
        });
    }

    renderFAQs() {
        const container = document.getElementById('faq-container');
        const noResults = document.getElementById('no-results');
        const resultsCount = document.getElementById('results-count');
        
        if (this.filteredFAQs.length === 0) {
            container.style.display = 'none';
            noResults.style.display = 'block';
            resultsCount.textContent = '0 articles found';
            return;
        }

        container.style.display = 'block';
        noResults.style.display = 'none';
        resultsCount.textContent = `${this.filteredFAQs.length} article${this.filteredFAQs.length === 1 ? '' : 's'} found`;
        
        container.innerHTML = this.filteredFAQs.map(faq => {
            const categoryInfo = this.data.categories.find(cat => cat.id === faq.category);
            const categoryName = categoryInfo ? categoryInfo.name : 'General';
            const categoryIcon = categoryInfo ? categoryInfo.icon : '❓';
            
            return `
                <div class="faq-item" data-faq-id="${faq.id}">
                    <div class="faq-header" onclick="this.toggleFAQ(${faq.id})">
                        <div class="faq-question">
                            <span class="faq-category">${categoryIcon} ${categoryName}</span>
                            <h4>${this.highlightSearchTerms(faq.question)}</h4>
                        </div>
                        <div class="faq-toggle">
                            <i class="fas fa-chevron-down"></i>
                        </div>
                    </div>
                    <div class="faq-content" id="faq-content-${faq.id}">
                        <div class="faq-answer">
                            <p>${this.highlightSearchTerms(faq.answer)}</p>
                        </div>
                        <div class="faq-meta">
                            <div class="faq-tags">
                                ${faq.tags.map(tag => `<span class="tag">#${tag}</span>`).join('')}
                            </div>
                            <div class="faq-actions">
                                <span class="last-updated">Updated: ${new Date(faq.last_updated).toLocaleDateString()}</span>
                                <div class="helpful-buttons">
                                    <button class="helpful-btn" onclick="this.markHelpful(${faq.id}, true)" title="Was this helpful?">
                                        <i class="fas fa-thumbs-up"></i>
                                    </button>
                                    <button class="helpful-btn" onclick="this.markHelpful(${faq.id}, false)" title="Was this helpful?">
                                        <i class="fas fa-thumbs-down"></i>
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            `;
        }).join('');
    }

    renderContactInfo() {
        document.getElementById('contact-email').textContent = this.data.contact.email;
        document.getElementById('contact-phone').textContent = this.data.contact.phone;
        document.getElementById('contact-hours').textContent = this.data.contact.hours;
        document.getElementById('response-time').textContent = this.data.contact.response_time;
        document.getElementById('email-link').href = `mailto:${this.data.contact.email}`;
    }

    setupEventListeners() {
        // Search functionality
        const searchInput = document.getElementById('search-input');
        const searchClear = document.getElementById('search-clear');
        
        searchInput.addEventListener('input', (e) => {
            this.searchQuery = e.target.value.toLowerCase();
            this.filterFAQs();
            searchClear.style.display = e.target.value ? 'block' : 'none';
        });

        searchClear.addEventListener('click', () => {
            searchInput.value = '';
            this.searchQuery = '';
            searchClear.style.display = 'none';
            this.filterFAQs();
            searchInput.focus();
        });

        // Category filter
        const categoryContainer = document.getElementById('category-filter');
        categoryContainer.addEventListener('click', (e) => {
            if (e.target.classList.contains('category-btn')) {
                // Remove active class from all buttons
                categoryContainer.querySelectorAll('.category-btn').forEach(btn => {
                    btn.classList.remove('active');
                });
                
                // Add active class to clicked button
                e.target.classList.add('active');
                
                // Update current category and filter
                this.currentCategory = e.target.getAttribute('data-category');
                this.filterFAQs();
            }
        });

        // FAQ toggle functionality
        this.setupFAQToggle();
    }

    setupFAQToggle() {
        // Use event delegation since FAQ items are dynamically created
        document.addEventListener('click', (e) => {
            if (e.target.closest('.faq-header')) {
                const header = e.target.closest('.faq-header');
                const faqItem = header.closest('.faq-item');
                const faqId = faqItem.getAttribute('data-faq-id');
                this.toggleFAQ(parseInt(faqId));
            }
        });
    }

    toggleFAQ(faqId) {
        const content = document.getElementById(`faq-content-${faqId}`);
        const toggle = content.previousElementSibling.querySelector('.faq-toggle i');
        
        if (content.classList.contains('active')) {
            content.classList.remove('active');
            toggle.classList.remove('fa-chevron-up');
            toggle.classList.add('fa-chevron-down');
        } else {
            content.classList.add('active');
            toggle.classList.remove('fa-chevron-down');
            toggle.classList.add('fa-chevron-up');
        }
    }

    filterFAQs() {
        let filtered = this.data.faqs;

        // Filter by category
        if (this.currentCategory !== 'all') {
            filtered = filtered.filter(faq => faq.category === this.currentCategory);
        }

        // Filter by search query
        if (this.searchQuery) {
            filtered = filtered.filter(faq => {
                return faq.question.toLowerCase().includes(this.searchQuery) ||
                       faq.answer.toLowerCase().includes(this.searchQuery) ||
                       faq.tags.some(tag => tag.toLowerCase().includes(this.searchQuery));
            });
        }

        this.filteredFAQs = filtered;
        this.renderFAQs();
    }

    highlightSearchTerms(text) {
        if (!this.searchQuery) return text;
        
        const regex = new RegExp(`(${this.escapeRegExp(this.searchQuery)})`, 'gi');
        return text.replace(regex, '<mark>$1</mark>');
    }

    escapeRegExp(string) {
        return string.replace(/[.*+?^${}()|[\]\\]/g, '\\$&');
    }

    handleQuickAction(action) {
        if (action.startsWith('mailto:')) {
            window.location.href = action;
        } else if (action.startsWith('#')) {
            const element = document.getElementById(action.substring(1));
            if (element) {
                element.scrollIntoView({ behavior: 'smooth' });
            }
        } else {
            window.open(action, '_blank');
        }
    }

    markHelpful(faqId, isHelpful) {
        // In a real implementation, this would send data to a server
        // For now, just provide user feedback
        const message = isHelpful ? 'Thanks for your feedback!' : 'Thanks for letting us know. We\'ll work to improve this answer.';
        
        // Create a temporary toast notification
        this.showToast(message);
    }

    showToast(message) {
        const toast = document.createElement('div');
        toast.className = 'toast';
        toast.textContent = message;
        document.body.appendChild(toast);
        
        // Show toast
        setTimeout(() => toast.classList.add('show'), 100);
        
        // Hide and remove toast
        setTimeout(() => {
            toast.classList.remove('show');
            setTimeout(() => document.body.removeChild(toast), 300);
        }, 3000);
    }

    hideLoading() {
        const loadingScreen = document.getElementById('loading-screen');
        loadingScreen.style.opacity = '0';
        setTimeout(() => {
            loadingScreen.style.display = 'none';
        }, 300);
    }

    showError() {
        const loadingScreen = document.getElementById('loading-screen');
        loadingScreen.innerHTML = `
            <div class="error-message">
                <i class="fas fa-exclamation-triangle"></i>
                <h3>Oops! Something went wrong</h3>
                <p>We couldn't load the support content. Please try refreshing the page.</p>
                <button class="btn btn-primary" onclick="location.reload()">Refresh Page</button>
            </div>
        `;
    }
}

// Utility functions
function scrollToContact() {
    document.getElementById('contact-section').scrollIntoView({ behavior: 'smooth' });
}

// Initialize the app when DOM is loaded
document.addEventListener('DOMContentLoaded', () => {
    new TecheazSupport();
});