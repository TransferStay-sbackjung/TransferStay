import { useParams } from 'react-router-dom';

function AuthCallback() {
    const { token } = useParams();
    const navigate = useNavigate();

    useEffect(() => {
        if (token) {
            localStorage.setItem('token', token);
            console.log("OAuth Token:", token);
            alert('Login successful!');
            navigate('/');
        } else {
            console.error('Token not found in URL.');
            alert('Login failed!');
            navigate('/login');
        }
    }, [token, navigate]);

    return <div>Loading...</div>;
}

export default AuthCallback;
