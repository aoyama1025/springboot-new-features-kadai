const stripe = Stripe('pk_test_51R1RmRHh1PSXH5FcSAbPjuJEOM9fT5Vt0i2qdOwK1xWkebGvpTkfzysY7YjZ5l9LgIoZ1ik7kKETMekhYDM5ZJSZ00TFnZUcb0');
const paymentButton = document.querySelector('#paymentButton');

paymentButton.addEventListener('click', () => {
 stripe.redirectToCheckout({
   sessionId: sessionId
 })
});