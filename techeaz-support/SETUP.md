# Quick Setup Guide for Techeaz Support

## 🚀 Deploy to GitHub Pages in 5 Minutes

Follow these simple steps to get your Techeaz Support app live on GitHub Pages:

### Step 1: Create GitHub Repository

1. Go to [GitHub.com](https://github.com)
2. Click "+" → "New repository"
3. Name it `techeaz-support` (or any name you prefer)
4. Make it **Public** (required for free GitHub Pages)
5. ✅ Check "Add a README file"
6. Click "Create repository"

### Step 2: Upload Your Files

**Option A: GitHub Web Interface (Easiest)**
1. In your new repository, click "uploading an existing file"
2. Drag and drop all files from your `techeaz-support` folder:
   - `index.html`
   - `styles.css`
   - `app.js`
   - `content.json`
   - `README.md`
   - `.gitignore`
3. Write commit message: "Initial Techeaz Support setup"
4. Click "Commit changes"

**Option B: Git Commands (if you have Git installed)**
```bash
cd techeaz-support
git init
git add .
git commit -m "Initial Techeaz Support setup"
git remote add origin https://github.com/YOURUSERNAME/YOURREPOSITORY.git
git push -u origin main
```

### Step 3: Enable GitHub Pages

1. Go to your repository on GitHub
2. Click "Settings" tab
3. Scroll down to "Pages" in the left sidebar
4. Under "Source", select "Deploy from a branch"
5. Choose "main" branch
6. Choose "/ (root)" folder
7. Click "Save"

### Step 4: Access Your App

1. Wait 2-3 minutes for deployment
2. Your app will be available at:
   `https://YOURUSERNAME.github.io/REPOSITORY-NAME`

Example: `https://adsch.github.io/techeaz-support`

## 📝 Customize Your Content

### Quick Edits on GitHub

1. Go to your repository
2. Click on `content.json`
3. Click the "Edit" pencil icon
4. Make your changes
5. Scroll down and click "Commit changes"
6. Changes appear in 1-2 minutes

### What to Customize First

1. **App Information**
   ```json
   "app": {
     "name": "Your Company Support",
     "description": "Get help with your questions"
   }
   ```

2. **Contact Details**
   ```json
   "contact": {
     "email": "support@yourcompany.com",
     "phone": "+1-555-123-4567"
   }
   ```

3. **Add Your FAQs**
   - Replace the example questions with your own
   - Update categories to match your needs
   - Add relevant tags for better search

## 🎨 Branding (Optional)

### Change Colors
Edit `styles.css` and modify the `:root` section:
```css
:root {
  --primary-color: #your-brand-color;
  --primary-dark: #darker-shade;
}
```

### Update Logo/Icon
- Replace the headset icon in `index.html` (line 24)
- Find icons at [Font Awesome](https://fontawesome.com/icons)

## 📱 Test Your App

Before going live, test:
- ✅ Search functionality works
- ✅ Categories filter properly  
- ✅ Contact links work (email, phone)
- ✅ Mobile responsive design
- ✅ All your content displays correctly

## 🔧 Troubleshooting

**App not loading?**
- Check if all files uploaded correctly
- Verify `content.json` has valid JSON syntax
- Check browser console for errors

**Content not updating?**
- Wait 2-3 minutes after changes
- Hard refresh browser (Ctrl+F5 or Cmd+Shift+R)
- Check GitHub Pages status in repository settings

**Need help?**
- Check the main [README.md](README.md) for detailed documentation
- Review the `content.json` structure
- Validate JSON syntax at [JSONLint.com](https://jsonlint.com)

## 🎉 You're Done!

Your Techeaz Support app is now live and ready to help your users! 

Remember: Any time you edit `content.json` on GitHub, your changes will automatically appear on your live site within a few minutes.