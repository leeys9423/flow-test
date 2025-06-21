// 페이지 로드 시 데이터 로드
window.onload = function() {
    loadFixedExtensions();
    loadCustomExtensions();
    initEventListeners();
}

// 이벤트 리스너 초기화
function initEventListeners() {
    // Enter 키로 확장자 추가
    document.getElementById('extensionInput').addEventListener('keypress', function(e) {
        if (e.key === 'Enter') {
            addCustomExtension();
        }
    });
}

// 고정 확장자 목록 로드
async function loadFixedExtensions() {
    try {
        const response = await fetch('/api/v1/extensions/fixed');
        const data = await response.json();

        if (data.data) {
            renderFixedExtensions(data.data);
        }
    } catch (error) {
        showError('고정 확장자 로드 중 오류가 발생했습니다.');
    }
}

// 고정 확장자 렌더링
function renderFixedExtensions(extensions) {
    const container = document.getElementById('fixedExtensions');
    container.innerHTML = extensions.map(ext => `
        <label>
            <input type="checkbox" 
                   data-id="${ext.id}" 
                   data-was-checked="${ext.blocked}"
                   ${ext.blocked ? 'checked' : ''}>
            <span>${ext.extensionLower}</span>
        </label>
    `).join('');
}

// 고정 확장자 저장 (체크박스 상태들)
async function saveFixedExtensions() {
    const checkboxes = document.querySelectorAll('#fixedExtensions input[type="checkbox"]');
    const promises = [];

    for (let checkbox of checkboxes) {
        const wasChecked = checkbox.dataset.wasChecked === 'true';
        const isChecked = checkbox.checked;

        // 상태가 변경된 경우만 API 호출
        if (wasChecked !== isChecked) {
            const id = checkbox.dataset.id;
            promises.push(toggleFixedExtension(id));
        }
    }

    try {
        if (promises.length > 0) {
            await Promise.all(promises);
            showMessage('고정 확장자 설정이 저장되었습니다.');

            // 현재 상태 저장
            checkboxes.forEach(cb => {
                cb.dataset.wasChecked = cb.checked;
            });
        } else {
            showMessage('변경된 설정이 없습니다.');
        }
    } catch (error) {
        showError('저장 중 오류가 발생했습니다.');
    }
}

// 고정 확장자 토글
async function toggleFixedExtension(id) {
    const response = await fetch(`/api/v1/extensions/fixed/${id}/toggle`, {
        method: 'PATCH'
    });

    if (!response.ok) {
        throw new Error('Toggle failed');
    }
}

// 커스텀 확장자 목록 로드
async function loadCustomExtensions() {
    try {
        const response = await fetch('/api/v1/extensions/custom');
        const data = await response.json();

        if (data.data) {
            renderCustomExtensions(data.data);
        }
    } catch (error) {
        showError('커스텀 확장자 로드 중 오류가 발생했습니다.');
    }
}

// 커스텀 확장자 렌더링
function renderCustomExtensions(extensions) {
    const container = document.getElementById('customExtensions');
    const counter = document.getElementById('customCounter');

    container.innerHTML = extensions.map(ext => `
        <div class="custom-item">
            <span>${ext.extensionLower}</span>
            <button type="button" onclick="deleteCustomExtension(${ext.id})">×</button>
        </div>
    `).join('');

    counter.textContent = `${extensions.length}/200`;
}

// 커스텀 확장자 추가
async function addCustomExtension() {
    const input = document.getElementById('extensionInput');
    const extension = input.value.trim();

    if (!extension) {
        showError('확장자를 입력해주세요.');
        return;
    }

    try {
        const response = await fetch('/api/v1/extensions/custom', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ extension: extension })
        });

        const data = await response.json();

        if (response.ok) {
            input.value = '';
            loadCustomExtensions(); // 목록 새로고침
            showMessage(`확장자 '${extension}'가 추가되었습니다.`);
        } else {
            showError(data.message || '추가 중 오류가 발생했습니다.');
        }
    } catch (error) {
        showError('추가 중 오류가 발생했습니다.');
    }
}

// 커스텀 확장자 삭제
async function deleteCustomExtension(id) {
    if (!confirm('정말 삭제하시겠습니까?')) {
        return;
    }

    try {
        const response = await fetch(`/api/v1/extensions/custom/${id}`, {
            method: 'DELETE'
        });

        if (response.ok) {
            loadCustomExtensions(); // 목록 새로고침
            showMessage('확장자가 삭제되었습니다.');
        } else {
            showError('삭제 중 오류가 발생했습니다.');
        }
    } catch (error) {
        showError('삭제 중 오류가 발생했습니다.');
    }
}

// 메시지 표시 함수들
function showMessage(message) {
    const messageArea = document.getElementById('messageArea');
    messageArea.innerHTML = `<div class="message">${message}</div>`;
    setTimeout(() => messageArea.innerHTML = '', 3000);
}

function showError(message) {
    const messageArea = document.getElementById('messageArea');
    messageArea.innerHTML = `<div class="error">${message}</div>`;
    setTimeout(() => messageArea.innerHTML = '', 5000);
}