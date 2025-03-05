// // service-worker.js
// // 설치 단계에서 SW를 즉시 활성화하도록 설정
// self.addEventListener('install', event => {
//     event.waitUntil(self.skipWaiting());
// });
//
// // 활성화 단계에서 모든 클라이언트를 즉시 제어하도록 설정
// self.addEventListener('activate', event => {
//     event.waitUntil(self.clients.claim());
// });
//
//
// // Service Worker 내에서 토큰을 저장할 변수
// let accessToken = null;
//
// // 클라이언트로부터 토큰 전달 메시지 수신
// self.addEventListener('message', event => {
//     if (event.data && event.data.type === 'SET_TOKEN') {
//         accessToken = event.data.token;
//         console.log('Service Worker에 토큰 저장됨:', accessToken);
//     }
// });
//
//
// // fetch 이벤트를 가로채어 요청 헤더에 토큰 추가
// self.addEventListener('fetch', event => {
//     const originalRequest = event.request;
//     const modifiedHeaders = new Headers(originalRequest.headers); // 기존 요청 헤더 복제
//
//     // accessToken이 존재하면 Authorization 헤더 추가
//     if (accessToken) {
//         modifiedHeaders.set('Authorization', 'Bearer ' + accessToken);
//         console.log("Added Authorization header:", modifiedHeaders.get('Authorization'));
//     }
//
//     // GET, HEAD 요청은 body가 없으므로 body 옵션 생략
//     const requestOptions = {
//         method: originalRequest.method,
//         headers: modifiedHeaders,
//         // mode: 'cors',   // 원본 요청 모드를 'cors' 로 설정
//         mode: originalRequest.mode,
//         credentials: originalRequest.credentials,
//         redirect: originalRequest.redirect,
//         referrer: originalRequest.referrer
//     };
//
//     // POST나 다른 메서드에서 body가 필요한 경우에는 클론을 사용
//     if (originalRequest.method !== 'GET' && originalRequest.method !== 'HEAD') {
//         // 요청 본문은 한 번만 읽을 수 있으므로 클론된 요청의 body를 사용합니다.
//         requestOptions.body = originalRequest.clone().body;
//     }
//
//     const modifiedRequest = new Request(originalRequest.url, requestOptions);
//
//     event.respondWith(fetch(modifiedRequest));
// });


