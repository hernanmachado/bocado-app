package com.bocado.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bocado.model.Payment
import com.bocado.model.PaymentRequest
import com.bocado.model.CardDetails
import com.bocado.repository.PaymentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class PaymentUiState(
    val orderId: Int = 0,
    val amount: Double = 0.0,
    val paymentMethod: String = "CREDIT_CARD",
    val cardNumber: String = "",
    val expiryDate: String = "",
    val cvv: String = "",
    val holderName: String = "",
    val isProcessing: Boolean = false,
    val paymentResult: Payment? = null,
    val error: String? = null,
    val isPaymentSuccess: Boolean = false
)

class PaymentViewModel(private val paymentRepository: PaymentRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(PaymentUiState())
    val uiState: StateFlow<PaymentUiState> = _uiState.asStateFlow()

    fun initializePayment(orderId: Int, amount: Double) {
        _uiState.value = _uiState.value.copy(
            orderId = orderId,
            amount = amount
        )
    }

    fun updatePaymentMethod(method: String) {
        _uiState.value = _uiState.value.copy(paymentMethod = method)
    }

    fun updateCardNumber(number: String) {
        _uiState.value = _uiState.value.copy(cardNumber = number)
    }

    fun updateExpiryDate(date: String) {
        _uiState.value = _uiState.value.copy(expiryDate = date)
    }

    fun updateCvv(cvv: String) {
        _uiState.value = _uiState.value.copy(cvv = cvv)
    }

    fun updateHolderName(name: String) {
        _uiState.value = _uiState.value.copy(holderName = name)
    }

    fun validatePaymentData(): Boolean {
        val state = _uiState.value
        return when {
            state.orderId == 0 -> {
                _uiState.value = state.copy(error = "ID de pedido inválido")
                false
            }
            state.amount <= 0 -> {
                _uiState.value = state.copy(error = "Monto inválido")
                false
            }
            state.paymentMethod == "CREDIT_CARD" || state.paymentMethod == "DEBIT_CARD" -> {
                when {
                    state.cardNumber.length != 16 -> {
                        _uiState.value = state.copy(error = "Número de tarjeta debe tener 16 dígitos")
                        false
                    }
                    state.expiryDate.isEmpty() -> {
                        _uiState.value = state.copy(error = "Fecha de expiración requerida")
                        false
                    }
                    state.cvv.length != 3 -> {
                        _uiState.value = state.copy(error = "CVV debe tener 3 dígitos")
                        false
                    }
                    state.holderName.isEmpty() -> {
                        _uiState.value = state.copy(error = "Nombre del titular requerido")
                        false
                    }
                    else -> true
                }
            }
            else -> true
        }
    }

    fun processPayment() {
        if (!validatePaymentData()) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isProcessing = true, error = null)
            try {
                val state = _uiState.value
                val cardDetails = if (state.paymentMethod in listOf("CREDIT_CARD", "DEBIT_CARD")) {
                    CardDetails(
                        cardNumber = state.cardNumber,
                        expiryDate = state.expiryDate,
                        cvv = state.cvv,
                        holderName = state.holderName
                    )
                } else null

                val paymentRequest = PaymentRequest(
                    orderId = state.orderId,
                    amount = state.amount,
                    paymentMethod = state.paymentMethod,
                    cardDetails = cardDetails
                )

                val result = paymentRepository.processPayment(paymentRequest)
                result.onSuccess { payment ->
                    _uiState.value = _uiState.value.copy(
                        paymentResult = payment,
                        isProcessing = false,
                        isPaymentSuccess = payment.status == "APPROVED"
                    )
                    if (payment.status != "APPROVED") {
                        _uiState.value = _uiState.value.copy(
                            error = "Pago rechazado. Intenta con otro método."
                        )
                    }
                }.onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        error = error.message ?: "Error al procesar pago",
                        isProcessing = false
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Error desconocido",
                    isProcessing = false
                )
            }
        }
    }

    fun resetPaymentForm() {
        _uiState.value = PaymentUiState(
            orderId = _uiState.value.orderId,
            amount = _uiState.value.amount
        )
    }
}
