import React, { useState, useEffect } from 'react';
import NavBar from '../NavBar'; // 네비게이션 바 컴포넌트 임포트
import '../../styles/user/BalanceRefund.css'; // 환불 페이지 스타일 파일
import { useNavigate } from "react-router-dom";
import {API_BASE_URL} from "../../App";

const BalanceRefundPage = () => {
    const navigate = useNavigate();

    // 현재 잔액 상태
    const [currBalance, setCurrBalance] = useState({
        balance: 0, // 초기 잔액을 0으로 설정
    });

    // 잔액 가져오기
    useEffect(() => {
        const getBalance = async () => {
            const token = localStorage.getItem('token');
            if (!token) {
                navigate('/login');
            } else {
                try {
                    const balanceResponse = await fetch(`${API_BASE_URL}/api/v1/deposit`, {
                        method: "GET",
                        headers: {
                            Authorization: `Bearer ${token}`,
                        },
                    });

                    if (balanceResponse.ok) {
                        const data = await balanceResponse.json();
                        setCurrBalance({
                            balance: data.data, // 서버에서 받아온 잔액을 설정
                        });
                    } else {
                        console.error("Failed to fetch balance");
                    }
                } catch (error) {
                    console.error("Error fetching balance", error);
                }
            }
        };

        getBalance();
    }, [navigate]);

    const [refundAmount, setRefundAmount] = useState(''); // 환불 금액 상태
    const [refundHistory, setRefundHistory] = useState([]); // 환불 이력 상태

    // 환불 금액 입력 처리
    const handleInputChange = (e) => {
        setRefundAmount(e.target.value);
    };

    // 환불 처리
    const handleRefund = async () => {
        const token = localStorage.getItem('token');
        const amount = parseInt(refundAmount, 10);

        if (isNaN(amount) || amount <= 0 || amount > currBalance.balance) {
            alert('유효한 금액을 입력하세요.');
            return;
        }

        try {
            const response = await fetch(`${API_BASE_URL}/api/v1/deposit/refund`, {
                method: "POST",
                headers: {
                    Authorization: `Bearer ${token}`,
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(amount), // 환불할 금액을 전송
            });

            if (response.ok) {
                const data = await response.json();
                const updatedBalance = data.data; // 서버에서 반환된 최신 잔액

                // 잔액 업데이트
                setCurrBalance((prevState) => ({
                    ...prevState,
                    balance: updatedBalance,
                }));

                // 환불 이력 업데이트
                const newHistory = [
                    ...refundHistory,
                    { amount, date: new Date().toLocaleString() }
                ];
                setRefundHistory(newHistory);

                // 입력 필드 초기화
                setRefundAmount('');
                alert("환불이 완료되었습니다.");
            } else {
                console.error("Failed to refund");
            }
        } catch (error) {
            console.error("Error processing refund", error);
        }
    };

    return (
        <div>
            <NavBar /> {/* 네비게이션 바 */}
            <div className="balance-refund-page">
                {/* 잔액 섹션 */}
                <div className="balance-section">
                    <h2>현재 잔액</h2>
                    <p className="balance-amount">{currBalance.balance.toLocaleString()} 원</p>
                </div>

                {/* 환불 폼 섹션 */}
                <div className="refund-form">
                    <h3>잔액 환불</h3>
                    <input
                        type="number"
                        placeholder="환불할 금액을 입력하세요"
                        value={refundAmount}
                        onChange={handleInputChange}
                    />
                    <button className="refund-button" onClick={handleRefund}>환불하기</button>
                </div>

                {/* 환불 이력 섹션 */}
                <div className="refund-history">
                    <h3>환불 이력</h3>
                    {refundHistory.length > 0 ? (
                        <ul>
                            {refundHistory.map((entry, index) => (
                                <li key={index}>
                                    <span>{entry.date}</span> - <span>{entry.amount.toLocaleString()} 원</span>
                                </li>
                            ))}
                        </ul>
                    ) : (
                        <p>환불 이력이 없습니다.</p>
                    )}
                </div>
            </div>
        </div>
    );
};

export default BalanceRefundPage;
