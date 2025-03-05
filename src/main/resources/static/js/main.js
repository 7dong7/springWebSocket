// if ('serviceWorker' in navigator) {
//     // Service Worker 등록
//     navigator.serviceWorker.register('/service-worker.js', {scope:'/'})
//         .then(registration => {
//             console.log('Service Worker 등록 성공:', registration);
//
//             // SW가 준비될 때까지 기다림
//             navigator.serviceWorker.ready.then(() => {
//                 console.log("worker ready")
//             });
//
//             // controllerchange 이벤트로 SW 제어가 시작되면 토큰 전송
//             navigator.serviceWorker.addEventListener('controllerchange', () => {
//                 console.log('Service Worker가 제어를 시작했습니다.');
//                 sendTokenToSW();
//             });
//         })
//         .catch(error => {
//             console.error('Service Worker 등록 실패:', error);
//         });
//
//
//
//     function sendTokenToSW() {
//         const token = localStorage.getItem('accessToken');
//         console.log("보내는 토큰:", token); // 토큰 값이 잘 나오는지 확인
//
//         if (token && navigator.serviceWorker.controller) {
//             navigator.serviceWorker.controller.postMessage({
//                 type: 'SET_TOKEN',
//                 token: token
//             });
//             console.log('토큰을 Service Worker에 전송했습니다.');
//         } else {
//             console.log('Service Worker가 아직 제어중이 아닙니다.');
//         }
//     }
// }

