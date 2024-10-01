import React, { useState, useEffect } from 'react';
import NavBar from '../NavBar'; // 네비게이션 바 컴포넌트 임포트
import '../../styles/user/BalanceCharge.css';
import { useNavigate } from "react-router-dom";
import {API_BASE_URL} from "../../App";

const BalanceChargePage = () => {
    const navigate = useNavigate();

    const [currBalance, setCurrBalance] = useState({
        balance: 0, // 초기 잔액을 0으로 설정
    });

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

    const [chargeAmount, setChargeAmount] = useState(''); // 입력된 충전 금액
    const [chargeHistory, setChargeHistory] = useState([]); // 충전 이력

    // 충전 금액 입력 처리
    const handleInputChange = (e) => {
        setChargeAmount(e.target.value);
    };

    // 충전 처리
    const handleCharge = async () => {
        const token = localStorage.getItem('token');
        const amount = parseInt(chargeAmount, 10);

        if (isNaN(amount) || amount <= 0) {
            alert('유효한 금액을 입력하세요.');
            return;
        }

        try {
            const response = await fetch("`${API_BASE_URL}/api/v1/deposit/recharge`", {
                method: "POST",
                headers: {
                    Authorization: `Bearer ${token}`,
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(amount), // 충전할 금액을 전송
            });

            if (response.ok) {
                const data = await response.json();
                const updatedBalance = data.data;

                // 잔액 업데이트
                setCurrBalance((prevState) => ({
                    ...prevState,
                    balance: updatedBalance,
                }));

                // 충전 이력 업데이트
                const newHistory = [
                    ...chargeHistory,
                    { amount, date: new Date().toLocaleString() }
                ];
                setChargeHistory(newHistory);

                // 입력 필드 초기화
                setChargeAmount('');
                alert("충전이 완료되었습니다.");
            } else {
                console.error("Failed to charge");
            }
        } catch (error) {
            console.error("Error charging balance", error);
        }
    };

    return (
        <div>
            <NavBar /> {/* 네비게이션 바 */}
            <div className="balance-charge-page">
                {/* 잔액 섹션 */}
                <div className="balance-section">
                    <h2>현재 잔액</h2>
                    <p className="balance-amount">{currBalance.balance.toLocaleString()} 원</p>
                </div>

                {/* 충전 폼 섹션 */}
                <div className="charge-form">
                    <h3>잔액 충전</h3>
                    <input
                        type="number"
                        placeholder="충전할 금액을 입력하세요"
                        value={chargeAmount}
                        onChange={handleInputChange}
                    />
                    <button className="charge-button" onClick={handleCharge}>충전하기</button>
                </div>

                {/* 충전 이력 섹션 */}
                <div className="charge-history">
                    <h3>충전 이력</h3>
                    {chargeHistory.length > 0 ? (
                        <ul>
                            {chargeHistory.map((entry, index) => (
                                <li key={index}>
                                    <span>{entry.date}</span> - <span>{entry.amount.toLocaleString()} 원</span>
                                </li>
                            ))}
                        </ul>
                    ) : (
                        <p>충전 이력이 없습니다.</p>
                    )}
                </div>
            </div>
        </div>
    );
};

export default BalanceChargePage;
