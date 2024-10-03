import React, { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from 'axios';
import '../../styles/auction/BiddingForm.css';
import { API_BASE_URL } from "../../App"; // 스타일 임포트

const BiddingForm = () => {
    const { postId } = useParams();  // 경매 ID는 URL에서 추출
    const navigate = useNavigate();

    // 폼 상태 관리
    const [suggestPrice, setSuggestPrice] = useState('');
    const [maxPrice, setMaxPrice] = useState('');
    const [bidType, setBidType] = useState('MANUAL'); // 기본값: MANUAL
    const [errorMessage, setErrorMessage] = useState('');
    const [errorDetail, setErrorDetail] = useState('');
    const [successMessage, setSuccessMessage] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();

        const token = localStorage.getItem('token');
        const requestData = {
            suggestPrice: parseInt(suggestPrice),
            maxPrice: bidType === 'AUTO' ? parseInt(maxPrice) : parseInt(suggestPrice),  // 자동일 때만 maxPrice 포함
            bidType: bidType
        };

        try {
            const response = await axios.post(
                `${API_BASE_URL}/api/v1/auction/${postId}/bidding`,
                requestData,
                {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                }
            );
            setSuccessMessage('응찰이 완료되었습니다.');
            setErrorMessage('');
            setErrorDetail('');
            setTimeout(() => {
                navigate('/');
            }, 2000);
        } catch (error) {
            if (error.response && error.response.data) {
                const { message, detail } = error.response.data;

                // 전체 에러 메시지 처리
                setErrorMessage(message || '응찰 중 오류가 발생했습니다.');
                setErrorDetail(detail || '추가 정보가 없습니다.');
            } else {
                setErrorMessage('응찰 중 오류가 발생했습니다.');
                setErrorDetail('');
            }
            setSuccessMessage('');
        }
    };

    return (
        <div className="bidding-form">
            <h2>응찰하기</h2>
            <form onSubmit={handleSubmit}>
                <div>
                    <label htmlFor="suggestPrice">제안가:</label>
                    <input
                        type="number"
                        id="suggestPrice"
                        value={suggestPrice}
                        onChange={(e) => setSuggestPrice(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <label htmlFor="bidType">응찰 타입:</label>
                    <select
                        id="bidType"
                        value={bidType}
                        onChange={(e) => setBidType(e.target.value)}
                        required
                    >
                        <option value="MANUAL">수동 응찰</option>
                        <option value="AUTO">자동 응찰</option>
                    </select>
                </div>
                {bidType === 'AUTO' && (
                    <div>
                        <label htmlFor="maxPrice">최대 제안가:</label>
                        <input
                            type="number"
                            id="maxPrice"
                            value={maxPrice}
                            onChange={(e) => setMaxPrice(e.target.value)}
                            required={bidType === 'AUTO'}  // 자동 응찰일 경우 필수
                        />
                    </div>
                )}

                {/* 전체 에러 메시지 출력 */}
                {errorMessage && <p className="error-message">{errorMessage}</p>}
                {/* 상세 에러 메시지 출력 */}
                {errorDetail && <p className="error-detail">{errorDetail}</p>}
                {successMessage && <p className="success-message">{successMessage}</p>}

                <div className="button-group">
                    <button type="submit" className="submit-button">응찰하기</button>
                    <button type="button" className="cancel-button" onClick={() => navigate(-1)}>
                        취소
                    </button>
                </div>
            </form>
        </div>
    );
};

export default BiddingForm;
