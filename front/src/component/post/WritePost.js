import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios'; // API 요청을 위해 axios 임포트
import NavBar from '../NavBar'; // 네비게이션 바 임포트
import '../../styles/post/WritePost.css'; // 스타일 임포트

const WritePost = () => {
  const [formData, setFormData] = useState({
    title: '',
    price: 0,
    locationDepth1: '',
    locationDepth2: '',
    personnel: 0,
    checkInDate: '',
    checkOutDate: '',
    description: '',
    reservationPlatform: '',
    isAuction: false,
    reservationCode: '',
    reservationName: '',
    reservationPhone: '',
    status: 'ACTIVE',
    startDate: '',
    startTime: '',
    deadlineDate: '',
    deadlineTime: '',
    startPrice: 0,
    purchasePrice: 0,
  });

  const navigate = useNavigate();

  // 페이지 접근 시 토큰 확인
  useEffect(() => {
    const token = localStorage.getItem('token');
    if (!token) {
      // 토큰이 없으면 로그인 페이지로 리다이렉트
      alert('로그인이 필요합니다.');
      navigate('/login');
    }
  }, [navigate]);

  // 입력 값 변경 핸들러
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value,
    });
  };

  // 게시글 제출 처리
  const handleSubmit = async (e) => {
    e.preventDefault();
    const token = localStorage.getItem('token'); // 저장된 토큰 가져오기

    try {
      const response = await axios.post('http://localhost:8080/api/v1/assignment-posts', formData, {
        headers: {
          Authorization: `Bearer ${token}`, // 헤더에 토큰 포함
        },
      });
      alert('게시글이 성공적으로 작성되었습니다.');
      navigate('/'); // 작성 후 메인 페이지로 이동
    } catch (error) {
      console.error('게시글 작성 실패:', error);
      alert('게시글 작성에 실패했습니다.');
    }
  };

  return (
    <div>
      <NavBar /> {/* 네비게이션 바 추가 */}
      <div className="write-post">
        <h1>양도 글 작성하기</h1>
        <form onSubmit={handleSubmit} className="write-post-form">
          <div className="form-group">
            <label>제목</label>
            <input
              type="text"
              name="title"
              value={formData.title}
              onChange={handleInputChange}
              required
            />
          </div>

          <div className="form-group">
            <label>가격</label>
            <input
              type="number"
              name="price"
              value={formData.price}
              onChange={handleInputChange}
              required
            />
          </div>

          <div className="form-group">
            <label>지역 (대분류)</label>
            <input
              type="text"
              name="locationDepth1"
              value={formData.locationDepth1}
              onChange={handleInputChange}
              required
            />
          </div>

          <div className="form-group">
            <label>지역 (소분류)</label>
            <input
              type="text"
              name="locationDepth2"
              value={formData.locationDepth2}
              onChange={handleInputChange}
              required
            />
          </div>

          <div className="form-group">
            <label>인원수</label>
            <input
              type="number"
              name="personnel"
              value={formData.personnel}
              onChange={handleInputChange}
              required
            />
          </div>

          <div className="form-group">
            <label>체크인 날짜</label>
            <input
              type="date"
              name="checkInDate"
              value={formData.checkInDate}
              onChange={handleInputChange}
              required
            />
          </div>

          <div className="form-group">
            <label>체크아웃 날짜</label>
            <input
              type="date"
              name="checkOutDate"
              value={formData.checkOutDate}
              onChange={handleInputChange}
              required
            />
          </div>

          <div className="form-group">
            <label>예약 플랫폼</label>
            <input
              type="text"
              name="reservationPlatform"
              value={formData.reservationPlatform}
              onChange={handleInputChange}
              required
            />
          </div>

          <div className="form-group">
            <label>경매로 양도하기</label>
            <input
              type="checkbox"
              name="isAuction"
              checked={formData.isAuction}
              onChange={(e) => setFormData({ ...formData, isAuction: e.target.checked })}
            />
          </div>

          {formData.isAuction && (
            <>
              <div className="form-group">
                <label>경매 시작 날짜</label>
                <input
                  type="date"
                  name="startDate"
                  value={formData.startDate}
                  onChange={handleInputChange}
                  required={formData.isAuction}
                />
              </div>

              <div className="form-group">
                <label>경매 시작 시간</label>
                <input
                  type="time"
                  name="startTime"
                  value={formData.startTime}
                  onChange={handleInputChange}
                  required={formData.isAuction}
                />
              </div>

              <div className="form-group">
                <label>경매 마감 날짜</label>
                <input
                  type="date"
                  name="deadlineDate"
                  value={formData.deadlineDate}
                  onChange={handleInputChange}
                  required={formData.isAuction}
                />
              </div>

              <div className="form-group">
                <label>경매 마감 시간</label>
                <input
                  type="time"
                  name="deadlineTime"
                  value={formData.deadlineTime}
                  onChange={handleInputChange}
                  required={formData.isAuction}
                />
              </div>

              <div className="form-group">
                <label>시작 가격</label>
                <input
                  type="number"
                  name="startPrice"
                  value={formData.startPrice}
                  onChange={handleInputChange}
                  required={formData.isAuction}
                />
              </div>

              <div className="form-group">
                          <label>즉시 구매 가격</label>
                          <input
                            type="number"
                            name="purchasePrice"
                            value={formData.purchasePrice}
                            onChange={handleInputChange}
                            required
                          />
                        </div>
            </>
          )}



          <div className="form-group">
            <label>예약 코드</label>
            <input
              type="text"
              name="reservationCode"
              value={formData.reservationCode}
              onChange={handleInputChange}
              required
            />
          </div>

          <div className="form-group">
            <label>예약자 이름</label>
            <input
              type="text"
              name="reservationName"
              value={formData.reservationName}
              onChange={handleInputChange}
              required
            />
          </div>

          <div className="form-group">
            <label>예약자 전화번호</label>
            <input
              type="text"
              name="reservationPhone"
              value={formData.reservationPhone}
              onChange={handleInputChange}
              required
            />
          </div>

          <div className="form-group">
            <label>내용</label>
            <textarea
              name="description"
              value={formData.description}
              onChange={handleInputChange}
              placeholder="내용 입력"
              rows="5"
              required
            />
          </div>

          <button type="submit" className="submit-button">게시하기</button>
        </form>
      </div>
    </div>
  );
};

export default WritePost;
