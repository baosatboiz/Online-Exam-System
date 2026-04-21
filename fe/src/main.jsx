import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.jsx'

window.hideLoadingScreen = function () {
  const el = document.getElementById('loading-screen')
  if (!el) return
  el.classList.add('hidden')
  setTimeout(() => el.remove(), 600)
}

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <App />
  </StrictMode>,
)

requestAnimationFrame(() => {
  requestAnimationFrame(() => {
    window.hideLoadingScreen()
  })
})

window.addEventListener('load', window.hideLoadingScreen, { once: true })
