// 로그인 요청 -> access token, refresh token
async function login(username, password) {
    try {
        const response = await fetch('/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: new URLSearchParams({
                username: username,
                password: password
            })
        });

        if (!response.ok) { // 로그인 실패
            throw new Error('Login failed');
        }

        // Access Token은 응답 헤더에서 가져옴
        const accessToken = response.headers.get('Authorization'); // "Bearer {token}" 형식

        if (accessToken) {
            // 로컬 스토리지에 저장 (또는 변수에 저장해도 됨)
            localStorage.setItem('accessToken', accessToken);
            console.log('로그인 성공! access token 로컬 스토리지 저장됨');
        } else {
            throw new Error('No Access Token received');
        }
        return accessToken;
    } catch (error) {
        console.error('Error during login:', error);
    }
}


// 인증된 요청 함수
async function fetchProtectedData(url) {
                            //  경로 지정
    try {
        const accessToken = localStorage.getItem('accessToken');
        if (!accessToken) {
            throw new Error('No Access Token found');
        }
                                            // 지정 경로
        const response = await fetch(url, {
            method: 'GET',
            headers: {
                'Authorization': accessToken
            }
        });

        if (!response.ok) {
            throw new Error('Request failed');
        }

        const data = await response.text();
        console.log('Protected data:', data);
        return data;
    } catch (error) {
        console.error('Error fetching protected data:', error);
    }
}

// 실행
async function main(username, password, url) {
    console.log("main: ", username, password, url);
    await login(username, password); // 로그인
    await fetchProtectedData(url);     // 보호된 데이터 요청
}