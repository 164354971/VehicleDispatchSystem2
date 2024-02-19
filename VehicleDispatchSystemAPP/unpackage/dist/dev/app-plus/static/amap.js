export function loadAmap() {
  return new Promise((resolve, reject) => {
    if (window.AMap) {
      resolve(window.AMap)
    } else {
      const script = document.createElement('script')
      script.type = 'text/javascript'
      script.src = 'https://webapi.amap.com/maps?v=1.4.15&key=YOUR_AMAP_API_KEY'
      script.onerror = reject
      script.onload = () => {
        resolve(window.AMap)
      }
      document.head.appendChild(script)
    }
  })
}