# Techeaz Support

A modern, responsive support app that uses GitHub-hosted JSON for easy content management. Perfect for maintaining FAQs, announcements, and support information that can be updated by editing a simple JSON file on GitHub.

## Features

- 📱 **Responsive Design** - Works perfectly on desktop, tablet, and mobile devices
- 🔍 **Smart Search** - Search through questions, answers, and tags with highlighting
- 🏷️ **Category Filtering** - Organize and filter FAQs by categories
- 📢 **Announcements** - Display important notices and updates
- ⚡ **Quick Actions** - Easy access to common support tasks
- 🎨 **Professional UI** - Clean, modern design with smooth animations
- 📝 **Easy Content Management** - Update content by editing JSON on GitHub

## Quick Start

### Option 1: GitHub Pages (Recommended)

1. Fork or clone this repository to your GitHub account
2. Enable GitHub Pages in your repository settings
3. Edit the `content.json` file to customize your content
4. Your support app will be available at `https://yourusername.github.io/techeaz-support`

### Option 2: Local Development

1. Download or clone the repository
2. Open `index.html` in a web browser
3. For development with live server, use VS Code Live Server extension or similar

## Content Management

### Editing the JSON Configuration

All content is managed through the `content.json` file. Here's how to update different sections:

#### App Information
```json
{
  "app": {
    "name": "Your Support App Name",
    "description": "Your app description",
    "version": "1.0.0"
  }
}
```

#### Contact Information
```json
{
  "contact": {
    "email": "support@yourcompany.com",
    "phone": "+1-555-YOUR-PHONE",
    "hours": "Monday - Friday: 9:00 AM - 6:00 PM EST",
    "response_time": "We typically respond within 24 hours"
  }
}
```

#### Categories
```json
{
  "categories": [
    {
      "id": "unique-category-id",
      "name": "Category Display Name",
      "description": "Category description",
      "icon": "📱"
    }
  ]
}
```

#### FAQ Items
```json
{
  "faqs": [
    {
      "id": 1,
      "category": "category-id",
      "question": "Your question here?",
      "answer": "Your detailed answer here.",
      "tags": ["tag1", "tag2", "tag3"],
      "helpful": 0,
      "last_updated": "2024-01-15"
    }
  ]
}
```

#### Announcements
```json
{
  "announcements": [
    {
      "id": 1,
      "type": "info",  // "info", "warning", or "error"
      "title": "Announcement Title",
      "message": "Announcement message content",
      "date": "2024-01-15",
      "active": true  // Set to false to hide
    }
  ]
}
```

#### Quick Actions
```json
{
  "quick_actions": [
    {
      "title": "Action Title",
      "description": "Action description",
      "action": "mailto:support@company.com",  // URL or action
      "icon": "📧"
    }
  ]
}
```

### Supported Action Types

Quick actions support different types of links:
- **Email**: `mailto:support@company.com`
- **External Links**: `https://example.com`
- **Internal Links**: `#section-id` (scrolls to element)

### Using Emojis

The app supports emojis for icons throughout the interface:
- 📱 Mobile/Apps
- 🔧 Technical
- 💰 Billing
- 🚀 Getting Started  
- ❓ General Questions
- 📧 Email
- 📞 Phone
- 💡 Ideas
- ⚠️ Warnings
- ✅ Success

## GitHub Workflow

### For Repository Owners

1. **Direct Editing**: Edit `content.json` directly on GitHub
2. **Branch & PR**: Create a branch, make changes, and merge via pull request
3. **GitHub Desktop**: Use GitHub Desktop for local editing and syncing

### For Team Members

1. **Fork the Repository**: Create your own copy
2. **Make Changes**: Edit the JSON file
3. **Create Pull Request**: Submit changes for review
4. **Merge**: Once approved, changes go live automatically

## File Structure

```
techeaz-support/
├── index.html          # Main HTML file
├── styles.css          # CSS styles
├── app.js             # JavaScript functionality
├── content.json       # Content configuration
└── README.md          # This documentation
```

## Customization

### Styling

Edit `styles.css` to customize the appearance:
- Change colors by modifying CSS custom properties in `:root`
- Adjust spacing, fonts, and layout as needed
- All styles are responsive and mobile-friendly

### Functionality

Modify `app.js` to add new features:
- Custom search algorithms
- Additional filtering options
- Integration with external APIs
- Analytics tracking

## Browser Support

- Chrome 90+
- Firefox 88+
- Safari 14+
- Edge 90+
- Mobile browsers (iOS Safari, Chrome Mobile)

## Performance

The app is designed for optimal performance:
- Lightweight (~50KB total)
- No external dependencies (except Font Awesome for icons)
- Fast loading and smooth animations
- Efficient search and filtering

## SEO & Accessibility

- Semantic HTML structure
- ARIA labels and roles
- Keyboard navigation support
- Screen reader friendly
- Mobile-optimized

## Production Deployment

### GitHub Pages Setup

1. Go to your repository settings
2. Scroll to "Pages" section
3. Select "Deploy from a branch"
4. Choose "main" branch and "/ (root)" folder
5. Save and wait for deployment

### Custom Domain (Optional)

1. Add a `CNAME` file with your domain name
2. Configure your DNS to point to GitHub Pages
3. Update repository settings with custom domain

### CDN Configuration

For better performance with GitHub Pages:
- Files are automatically served via GitHub's CDN
- Enable caching headers for static assets
- Consider using a custom CDN for high-traffic sites

## Troubleshooting

### Common Issues

**Content not updating**: 
- Check JSON syntax with a validator
- Ensure GitHub Pages has rebuilt (can take a few minutes)
- Clear browser cache

**Search not working**:
- Verify JavaScript is enabled
- Check browser console for errors
- Ensure content.json is accessible

**Styling issues**:
- Verify CSS file is loading
- Check for conflicting styles
- Test on different browsers

### Support

For issues with this template:
1. Check the [GitHub Issues](../../issues) page
2. Create a new issue with details
3. Include browser version and error messages

## License

MIT License - feel free to use for personal or commercial projects.

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## Changelog

### v1.0.0
- Initial release
- Core FAQ functionality
- Search and filtering
- Responsive design
- GitHub Pages support